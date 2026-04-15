$file = "c:\Users\Asus\OneDrive\Desktop\IPSM_Little_Wonders_School-master\IPSM_Little_Wonders_School-master\target\littlewonders-0.0.1-SNAPSHOT.jar"
$chunkSize = 5MB
$stream = [System.IO.File]::OpenRead($file)
$buffer = New-Object byte[] $chunkSize
$part = 0
while ($read = $stream.Read($buffer, 0, $buffer.Length)) {
    $out = "c:\Users\Asus\OneDrive\Desktop\IPSM_Little_Wonders_School-master\IPSM_Little_Wonders_School-master\target\part$part"
    if ($read -eq $chunkSize) {
        [System.IO.File]::WriteAllBytes($out, $buffer)
    } else {
        $lastBuffer = New-Object byte[] $read
        [System.Array]::Copy($buffer, $lastBuffer, $read)
        [System.IO.File]::WriteAllBytes($out, $lastBuffer)
    }
    $part++
}
$stream.Close()
Write-Host "Split into $part parts."