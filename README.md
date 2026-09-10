# Android VHAL test app

This is a test app, to test AOSP OEM VHAL with dynamic permissions.

## Vehicle data source configuration

Vehicle data sources are configured in `platform/app_config.json` and store in
`/vendor/etc/vhalmap/` on the device.

The file defines two connections:

- `mock` using WebSocket on port `8126` from [COVESA/aosp_vhal_server_mock](https://github.com/COVESA/aosp_vhal_server_mock)
- `kuksa` using gRPC on port `55557` from [eclipse-kuksa/kuksa-databroker](https://github.com/eclipse-kuksa/kuksa-databroker)


## Permission Grant Service Proxy configuration

Permission Grant Service Proxy from
[COVESA/aosp_packages_services_oem](https://github.com/COVESA/aosp_packages_services_oem)
is configured in
`platform/vhal-privapp-permissions-permissionsgrantproxyservice-trusted-external.xml` and store in
`/system/etc/permissions/` on the device.


## Update platform files on the emulator

Start the emulator with writable system using AVD with `-writable-system`:

```sh
emulator \
  -avd COVESA_gRPC_Automotive_1408p_landscape \
  -writable-system \
  -no-snapshot-load \
  -no-snapshot-save \
  -no-boot-anim
```

### Update `app_config.json`:

```sh
adb root && adb remount
adb push platform/app_config.json /vendor/etc/vhalmap/app_config.json
adb reboot
```

### Update `app_config.json`:

```sh
adb root && adb remount
adb push platform/vhal-privapp-permissions-permissionsgrantproxyservice-trusted-external.xml \
  /system/etc/permissions/vhal-privapp-permissions-permissionsgrantproxyservice-trusted-external.xml
adb reboot
```
