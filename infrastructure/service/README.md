# kotlin-minimal-server service

Instructions for installing kotlin-minimal-server as a service on Ubuntu.

## Prerequisites

- kotlin-minimal jar
- JRE 21

## Installation

```bash
sudo cp km.server /etc/systemd/system/
sudo systemctl daemon-reload
sudo systemctl enable km.service
```

## Start the service

```bash
sudo systemctl start km.service
```

## Check the service status

```bash
sudo systemctl status km.service
```

## Stop the service

```bash
sudo systemctl stop km.service
```

## Uninstall the service

```bash
sudo systemctl stop km.service
sudo systemctl disable km.service
sudo rm /etc/systemd/system/km.service
sudo systemctl daemon-reload
```

## Troubleshooting

- Check the logs: `journalctl -u km.service`

### km.service: Main process exited, code=exited, status=217/USER

Indicates the wrong user is being used to run the service. Update the `User` and `Group` in `km.service` to the correct user and group.