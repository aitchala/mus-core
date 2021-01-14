SUMMARY = "Driver for Realtek USB wireless device 8192eu"
HOMEPAGE = "http://www.realtek.com/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://README.md;md5=59ef6f2246904f7919c0b0d0a81156b1"


SRC_URI = "git://github.com/Mange/rtl8192eu-linux-driver.git \
          file://rtl8192eu-makefile.patch \
          file://rtl8192eu-gcc5.patch \
          file://CHECKSM_IPV6_H.patch \
          "

SRCREV = "1387cf623d54bc2caec533e72ee18ef3b6a1db29"


S = "${WORKDIR}/git"

inherit module siteinfo

EXTRA_OEMAKE = "CONFIG_RTL8192EU=m"

do_configure() {
        sed -e "s/^CONFIG_PLATFORM_I386_PC.*=.*y/EXTRA_CFLAGS += -Wno-date-time -DCONFIG_${@oe.utils.conditional('SITEINFO_ENDIANNESS', 'le', 'LITTLE', 'BIG', d)}_ENDIAN/" -i Makefile
}
do_compile() {
        unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
        oe_runmake -C "${STAGING_KERNEL_DIR}" M="${S}" modules 
}

do_install() {
        install -d ${D}/lib/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
        install -m 0644 ${S}/8192eu.ko ${D}${base_libdir}/modules/${KERNEL_VERSION}/kernel/drivers/net/wireless
}


