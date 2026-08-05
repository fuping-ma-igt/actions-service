$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$fixture = Join-Path $PSScriptRoot "contract\ActionRetryPolicyContractTest.java"
$testDirectory = Join-Path $projectRoot "src\test\java\com\actions\service\retry"
$testFile = Join-Path $testDirectory "ActionRetryPolicyContractTest.java"

New-Item -ItemType Directory -Path $testDirectory -Force | Out-Null

if (Test-Path -LiteralPath $testFile) {
    throw "Contract-test target already exists: $testFile"
}

Copy-Item -LiteralPath $fixture -Destination $testFile

try {
    & (Join-Path $projectRoot "mvnw.cmd") test
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }
} finally {
    Remove-Item -LiteralPath $testFile -ErrorAction SilentlyContinue
}
