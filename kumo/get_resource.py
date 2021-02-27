from kumo.unix_permissions import UnixPermissions
from flask import Blueprint, g, send_from_directory, abort
from kumo.media_directories import resolve_path

bp = Blueprint("img", __name__)


@bp.route("/img/")
@bp.route("/img")
@bp.route("/img/<path:sub_path>")
def img(sub_path=None):
	if "permission" not in g:
		g.permission = UnixPermissions()

	if g.user is not None:
		if sub_path is not None:
			current_path = resolve_path(sub_path)
			filename = current_path.split("/")[-1]
			directory = current_path[0:len(current_path) - len(filename)]
			if g.permission.has_permission(current_path, g.user.id, g.user.admin):
				return send_from_directory(directory, filename=filename)

	abort(404)
