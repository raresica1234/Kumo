# Security
## General security
- Bcrypt
- OAuth authentication
- Ability to invalidate sessions
    - Sessions contain information such as ip and general location for differentiation
- Password reset
- Password change
- Register by invite - via email
- Ability to block/suspend account
- Temporarily suspend account on 3 failed login attempts
- Confirmation email on registration + activation (configurable)
## Permissions
- Ability to blacklist paths as an admin for a user
- Give specific permissions per folder (delete, move, rename, copy)
- Deletion marks as deleted and blacklists them, they will be deleted after a configurable expiration time
# Sharing
- Temporary share links (expire date/download times)
# Streaming
- Mkv streaming through websockets?
    - Select audio stream
    - Select video stream
    - Select subtitle stream
- Watch together feature (sync via websockets -- only after streaming through mkv works)
# Content viewing
- Thumbnail caching with redis
- Photo viewer
- Mp4 viewer (preferrably with streaming or in a configurable way that doesn't kill the bandwidth)
- Override context menu with custom actions
- File/folder uploading
- Download single file or automatically compress (configurable format - zip, gzip)
# Automatic backups
- Phone app that automatically syncs a folder with kumo BE only on wifi
- Scheduleable at specific hours
# Deploy
- Create dockerfile
- Create docker stack/docker compose file for everything
- Figure out a way of automatic update or prompting updates and showing to superuser