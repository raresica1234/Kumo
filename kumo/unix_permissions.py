from kumo.permission import Permission


class UnixPermissions(Permission):
    def __init__(self):
        super().__init__()

    def has_permission(self, directory, user_id, admin):
        if admin:
            for dir in self._media_dirs:
                if dir.root and directory.startswith(dir.path):
                    return True
            return False
        else:
            for dir in self._media_dirs:
                if directory.startswith(dir.path):
                    for permission in self._media_perms:
                        if permission.user_id == user_id and permission.media_id == dir.id:
                            return permission.permission_read
            return False

    def get_readable_root_directories(self, user_id, admin):
        media_dir = []

        if admin:
            for dir in self._media_dirs:
                if dir.root:
                    media_dir.append(dir.name)
        else:
            for dir in self._media_dirs:
                if dir.root:
                    for permission in self._media_perms:
                        if permission.user_id == user_id and permission.media_id == dir.id and permission.permission_read:
                            media_dir.append(dir.name)

        return media_dir
