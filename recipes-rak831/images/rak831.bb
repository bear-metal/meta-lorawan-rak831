DESCRIPTION = "A console-only image based on core-image and LoRaWan server software added"

IMAGE_FEATURES += "ssh-server-openssh"

KERNEL_IMAGE_INSTALL = "\
    kernel-module-spidev \
    kernel-module-brcmfmac \
    kernel-module-snd-bcm2835 \
    kernel-module-i2c-dev \
    kernel-module-i2c-bcm2835 \
    kernel-module-spi-bcm2835 \
    kernel-module-bcm2835-gpiomem \
    "

LORA_IMAGE_INSTALL = " \
    lora-gateway \
    lora-pkt-fwd \
    "

PACKAGES_IMAGE_INSTALL = " \
    linux-firmware-bcm43430 \
    wireless-tools \
    wpa-supplicant \
    avahi-daemon avahi-utils \
    "

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-full-cmdline \
    ${LORA_IMAGE_INSTALL} \
    ${PACKAGES_IMAGE_INSTALL} \
    ${KERNEL_IMAGE_INSTALL} \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    "

EXTRA_IMAGE_FEATURES_append = " read-only-rootfs"

ROOTFS_POSTPROCESS_COMMAND_append = " symlink_to_data; add_ssh_authorized_keys;"
symlink_to_data () {
  if ${@bb.utils.contains('DISTRO_FEATURES', 'mender-image', 'true', 'false', d)}; then
    # persist wpa_supplicant.conf to mender data partition
    ln -sf /data/wpa_supplicant.conf ${IMAGE_ROOTFS}${sysconfdir}/wpa_supplicant.conf

    # Change ssh host key locations. They get generated on-demand by the startup scripts.
    sed -i 's/\/var\/run\/ssh/\/data\/ssh/g' ${IMAGE_ROOTFS}${sysconfdir}/ssh/sshd_config_readonly
  fi
}
add_ssh_authorized_keys () {
  mkdir -p ${IMAGE_ROOTFS}/home/root/.ssh/
  echo ${SSH_AUTHORIZED_KEYS} > ${IMAGE_ROOTFS}/home/root/.ssh/authorized_keys
}

inherit core-image
