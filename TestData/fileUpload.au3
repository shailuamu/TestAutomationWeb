#include <Constants.au3>
		$windowHandle = WinGetHandle("Open")
        WinActivate($windowHandle)
   $text =$CmdLineRaw
		Send($text)
		Send("{Enter}")
