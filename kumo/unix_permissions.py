from kumo.permission import Permission


class UnixPermissions(Permission):
	def __init__(self):
		super().__init__()

	def has_permission(self, directory, user_id, admin):
		if admin:
			for media_directory in self._media_dirs:
				if media_directory.root and directory.startswith(media_directory.path):
					return True
			return False
		else:
			for media_directory in self._media_dirs:
				if directory.startswith(media_directory.path):
					for permission in self._media_perms:
						if permission.user_id == user_id and permission.media_id == directory.id:
							return permission.permission_read
			return False

	def get_readable_root_directories(self, user_id, admin):
		media_dir = []

		if admin:
			for directory in self._media_dirs:
				if directory.root:
					media_dir.append((directory.name, directory.path))
		else:
			for directory in self._media_dirs:
				if directory.root:
					for permission in self._media_perms:
						if permission.user_id == user_id and permission.media_id == directory.id and permission.permission_read:
							media_dir.append((directory.name, directory.path))

		return media_dir

	def get_root_directory_path(self, name):
		for directory in self._media_dirs:
			if directory.root:
				if name == directory.name:
					return directory.path
