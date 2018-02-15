Yocto image for the RAK831 LoRaWan gateway.

Read the <a href="http://www.yoctoproject.org/docs/current/yocto-project-qs/yocto-project-qs.html">Yocto getting started guide</a> to install poky.

# Layer Dependencies

* URI: git://github.com/sigysmund/meta-lora-net.git
  * branch: master
  * revision: HEAD
* URI: git://git.yoctoproject.org/poky
  * branch: master
  * revision: HEAD
* URI: git://git.openembedded.org/meta-openembedded
  * layers: meta-oe, meta-multimedia, meta-networking, meta-python
  * branch: master
  * revision: HEAD

##  Basic usage

Set up build environment in poky workdir:
```bash
source oe-init-build-env rak831
```

Set up `conf/bblayers.conf`
```bash
# POKY_BBLAYERS_CONF_VERSION is increased each time build/conf/bblayers.conf
# changes incompatibly
POKY_BBLAYERS_CONF_VERSION = "2"

BBPATH = "${TOPDIR}"
BBFILES ?= ""

BBLAYERS ?= " \
  /workdir/poky/meta \
  /workdir/poky/meta-poky \
  /workdir/poky/meta-yocto-bsp \
  /workdir/poky/meta-raspberrypi \
  /workdir/poky/meta-openembedded/meta-oe \
  /workdir/poky/meta-openembedded/meta-python \
  /workdir/poky/meta-lora-net \
  /workdir/poky/meta-lorawan-rak831 \
  "
```

Add your ssh pubkey and LoRaWan configs to `conf/local.conf`
```bash
MACHINE = "raspberrypi3"
hostname_pn-base-files = "rak831"
SSH_AUTHORIZED_KEYS = ""
LORA_GLOBAL_CONF = ''
LORA_LOCAL_CONF = ''

DISTRO ?= "poky"
PACKAGE_CLASSES ?= "package_rpm"

USER_CLASSES ?= "buildstats image-mklibs image-prelink"

PATCHRESOLVE = "noop"

BB_DISKMON_DIRS ??= "\
    STOPTASKS,${TMPDIR},1G,100K \
    STOPTASKS,${DL_DIR},1G,100K \
    STOPTASKS,${SSTATE_DIR},1G,100K \
    STOPTASKS,/tmp,100M,100K \
    ABORT,${TMPDIR},100M,1K \
    ABORT,${DL_DIR},100M,1K \
    ABORT,${SSTATE_DIR},100M,1K \
    ABORT,/tmp,10M,1K"

# CONF_VERSION is increased each time build/conf/ changes incompatibly and is used to
# track the version of this file when it was generated. This can safely be ignored if
# this doesn't mean anything to you.
CONF_VERSION = "1"
```

Build the image
```bash
bitbake rak831
```

Use dd to flash the resulting image to your sdcard
```bash
sudo dd if=tmp/deploy/images/raspberrypi3/rak831-raspberrypi3.rpi-sdimg of=/dev/dev_of_sdcard bs=1m && sudo sync
```

##  As an <a href="https://mender.io/">Mender</a> image
xxx