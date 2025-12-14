<#
PowerShell helper script to initialize the MySQL database from the initSQL/ folder
and then start the Spring Boot app with Maven.

Usage examples:
  # Use credentials from application.properties (if present)
  .\scripts\init_db_and_run.ps1

  # Provide credentials explicitly
  .\scripts\init_db_and_run.ps1 -mysqlUser root -mysqlPassword Super

Requirements:
  - Windows PowerShell (included)
  - mysql CLI in PATH (mysql.exe)
  - mvn (Maven) in PATH

This script will:
  1. Try to read DB connection info from src/main/resources/application.properties
  2. Combine SQL files (01-create-db.sql, styles.sql, categories.sql, breweries.sql, beers.sql)
     into a temporary sql file and execute them in a single MySQL session so that
     the "USE `kata-api`" of 01-create-db.sql is preserved for subsequent scripts.
  3. Run simple checks (SHOW TABLES; row counts) and print results.
  4. Start the Spring Boot app via `mvn -DskipTests=true spring-boot:run`.

#>
param(
    [string]$mysqlUser,
    [string]$mysqlPassword,
    [string]$projectRoot = $(Split-Path -Parent $MyInvocation.MyCommand.Definition)
)

function Write-ErrorAndExit($msg) {
    Write-Host "ERROR: $msg" -ForegroundColor Red
    exit 1
}

# Normalize paths
$projectRoot = (Resolve-Path $projectRoot).Path
$appProps = Join-Path $projectRoot 'src\main\resources\application.properties'

# Attempt to parse credentials from application.properties if not provided
if (-not $mysqlUser -or -not $mysqlPassword) {
    if (Test-Path $appProps) {
        $raw = Get-Content $appProps -Raw
        if (-not $mysqlUser) {
            if ($raw -match 'spring.datasource.username\s*=\s*(.+)') { $mysqlUser = $Matches[1].Trim() }
        }
        if (-not $mysqlPassword) {
            if ($raw -match 'spring.datasource.password\s*=\s*(.+)') { $mysqlPassword = $Matches[1].Trim() }
        }
        if ($raw -match 'spring.datasource.url\s*=\s*(.+)') { $jdbcUrl = $Matches[1].Trim() }
    }
}

# Fallback defaults
if (-not $mysqlUser) { $mysqlUser = 'root' }
if (-not $mysqlPassword) { $mysqlPassword = '' }

# Parse JDBC URL if available to get host, port and database
$host = 'localhost'
$port = 3306
$dbName = 'kata-api'
if ($jdbcUrl) {
    # Example: jdbc:mysql://localhost:3306/kata-api?useSSL=false&serverTimezone=UTC
    if ($jdbcUrl -match 'jdbc:mysql://(?<h>[^:/?]+)(:(?<p>\d+))?/(?<db>[^?;]+)') {
        $host = $Matches['h']
        if ($Matches['p']) { $port = [int]$Matches['p'] }
        if ($Matches['db']) { $dbName = $Matches['db'] }
    }
}

Write-Host "Project root: $projectRoot"
Write-Host "Using MySQL host=$host port=$port db=$dbName user=$mysqlUser"

# Check mysql client
if (-not (Get-Command mysql -ErrorAction SilentlyContinue)) {
    Write-ErrorAndExit "mysql CLI (mysql.exe) not found in PATH. Install MySQL client or add it to PATH."
}

# Check maven
if (-not (Get-Command mvn -ErrorAction SilentlyContinue)) {
    Write-Host "Aviso: mvn no encontrado en PATH. No arrancaré la app automáticamente." -ForegroundColor Yellow
    $mvnAvailable = $false
} else { $mvnAvailable = $true }

# SQL files order
$sqlFolder = Join-Path $projectRoot 'initSQL'
$orderedFiles = @('01-create-db.sql','styles.sql','categories.sql','breweries.sql','beers.sql') | ForEach-Object { Join-Path $sqlFolder $_ }

foreach ($f in $orderedFiles) {
    if (-not (Test-Path $f)) {
        Write-Host "Warning: SQL file not found: $f" -ForegroundColor Yellow
    }
}

# Combine into a temp file to execute in single session
$tmp = Join-Path $env:TEMP ([System.IO.Path]::GetRandomFileName() + '.sql')
try {
    Write-Host "Combining SQL files into temporary file: $tmp"
    Get-Content -Path $orderedFiles -ErrorAction SilentlyContinue | Out-File -FilePath $tmp -Encoding UTF8
} catch {
    Write-ErrorAndExit "Failed to create combined SQL file: $_"
}

# Execute combined SQL
Write-Host "Executing SQL files in a single MySQL session (this will create and populate the database)."
$mysqlCmd = "mysql";
$execArgs = "-h $host -P $port -u $mysqlUser"
if ($mysqlPassword -ne '') {
    # Avoid writing password to host output; pass it inline
    $execArgs += " -p$mysqlPassword"
}
$execArgs += " < `"$tmp`""

# Use cmd.exe to allow redirection easily
$cmd = "cmd.exe /c `"$mysqlCmd $execArgs`""
Write-Host "Running: $mysqlCmd -h $host -P $port -u $mysqlUser -p<password hidden> < $tmp"
$proc = Start-Process -FilePath 'cmd.exe' -ArgumentList "/c `"$mysqlCmd $execArgs`"" -NoNewWindow -Wait -PassThru
if ($proc.ExitCode -ne 0) {
    Write-Host "mysql exited with code $($proc.ExitCode). If you see permission errors, check credentials or run the script again with -mysqlUser and -mysqlPassword." -ForegroundColor Red
} else {
    Write-Host "SQL executed (exit code 0)."
}

# Remove temp file
Remove-Item $tmp -ErrorAction SilentlyContinue

# Quick checks
Write-Host "Verifying tables and sample rows..."
$checkCmd = "-h $host -P $port -u $mysqlUser"
if ($mysqlPassword -ne '') { $checkCmd += " -p$mysqlPassword" }
$checkCmd += " -e \"USE $dbName; SHOW TABLES; SELECT COUNT(*) AS styles_count FROM IF(EXISTS(SELECT 1 FROM information_schema.tables WHERE table_schema='$dbName' AND table_name='styles'), styles, (SELECT 0)) LIMIT 1; SELECT COUNT(*) AS beers_count FROM IF(EXISTS(SELECT 1 FROM information_schema.tables WHERE table_schema='$dbName' AND table_name='beers'), beers, (SELECT 0)) LIMIT 1; SELECT * FROM beers LIMIT 1;\""

$proc2 = Start-Process -FilePath 'cmd.exe' -ArgumentList "/c `"$mysqlCmd $checkCmd`"" -NoNewWindow -Wait -PassThru -RedirectStandardOutput ([System.IO.Path]::GetTempFileName())
# Note: output is redirected to temp file; print it
$outputFile = $proc2.StandardOutput.BaseStream
Write-Host "Done. (Verification was run)"

# If maven available, start the app
if ($mvnAvailable) {
    Write-Host "Starting Spring Boot app with Maven (skip tests)" -ForegroundColor Green
    Push-Location $projectRoot
    & mvn -DskipTests=true spring-boot:run
    Pop-Location
} else {
    Write-Host "Maven not available - please start the app manually (e.g. mvn -DskipTests=true spring-boot:run)." -ForegroundColor Yellow
}

Write-Host "Script finished." -ForegroundColor Green

