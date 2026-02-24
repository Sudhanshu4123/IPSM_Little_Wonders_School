
param($partNumber)
$file = "c:\Users\Asus\OneDrive\Desktop\IPSM_Little_Wonders_School-master\IPSM_Little_Wonders_School-master\target\part$partNumber"
$outFile = "c:\Users\Asus\OneDrive\Desktop\IPSM_Little_Wonders_School-master\IPSM_Little_Wonders_School-master\part$partNumber.b64"
[IO.File]::WriteAllText($outFile, [Convert]::ToBase64String([IO.File]::ReadAllBytes($file)))
