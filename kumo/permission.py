from kumo.models import db, MediaDirectory, MediaPermissions


class Permission:
    def __init__(self):
        self._media_dirs = MediaDirectory.query.all()
        self._media_dirs.sort(key=lambda media_dir: len(media_dir.path.split("/")) - 1, reverse=True)
        for dir in self._media_dirs:
            print(dir.path)
        self._media_perms = MediaPermissions.query.all()

    def has_permission(self, directory, user_id, admin):
        raise NotImplementedError()

    def get_readable_root_directories(self, user_id, admin):
        raise NotImplementedError()


