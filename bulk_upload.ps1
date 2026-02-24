
$parts = 0..19
foreach ($part in $parts) {
    Write-Host "Processing part $part..."
    & ".\upload_part.ps1" -partNumber $part -password "iPSMNURSING@2024#" -serverIp "72.61.253.79"
}
