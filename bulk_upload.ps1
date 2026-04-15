
$partsCount = @(Get-ChildItem -Path "$PSScriptRoot\target\part*" -File).Count
$parts = 0..($partsCount - 1)
foreach ($part in $parts) {
    Write-Host "Processing part $part..."
    & "$PSScriptRoot\upload_part.ps1" -partNumber $part -password "iPSMNURSING@2024#" -serverIp "72.61.253.79"
}
