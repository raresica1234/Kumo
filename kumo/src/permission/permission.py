from kumo.src.model.media_directory import MediaDirectory
from kumo.src.model.media_permission import MediaPermission


class Permission:
	def __init__(self):
		self._media_dirs = MediaDirectory.query.all()
		self._media_dirs.sort(key=lambda media_dir: len(media_dir.path.split("/")) - 1, reverse=True)
		self._media_perms = MediaPermission.query.all()

	def has_permission(self, directory, user_id, admin):
		raise NotImplementedError()

	def get_readable_root_directories(self, user_id, admin):
		raise NotImplementedError()

	def get_root_directory_path(self, name):
		raise NotImplementedError()
