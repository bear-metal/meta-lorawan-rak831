FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_append = " file://lora_pkt_fwd.service \
                 "

inherit systemd

SYSTEMD_SERVICE_${PN} = "lora_pkt_fwd.service"
FILES_${PN} += "${systemd_unitdir}/system/lora_pkt_fwd.service \
                ${sysconfdir}/lora_pkt_fwd/global_conf.json \
                ${sysconfdir}/lora_pkt_fwd/local_conf.json \
               "

do_install_append() {
    install -d ${D}${sysconfdir}/lora_pkt_fwd
    echo '${LORA_GLOBAL_CONF}' > ${D}${sysconfdir}/lora_pkt_fwd/global_conf.json
    echo '${LORA_LOCAL_CONF}' > ${D}${sysconfdir}/lora_pkt_fwd/local_conf.json

    install -d ${D}/${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/lora_pkt_fwd.service ${D}/${systemd_unitdir}/system
}