# have to remove serial console (added by linux-raspberrypi.inc) since rak831 gps is connected to it
CMDLINE_remove = "console=serial0,115200"
