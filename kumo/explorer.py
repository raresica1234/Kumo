import os
from kumo.unix_permissions import UnixPermissions
from flask import Blueprint, jsonify, g, render_template, redirect, url_for

bp = Blueprint("explorer", __name__)


@bp.route("/")
@bp.route("/<path:sub_path>")
def explore(sub_path=None):
	if g.user is None:
		return redirect(url_for("auth.login"))

	if "permission" not in g:
		g.permission = UnixPermissions()

	file_data = []
	files = []
	if g.user is not None:
		if sub_path is None:
			directories = g.permission.get_readable_root_directories(g.user.id, g.user.admin)
			for name, path in directories:
				file_type = os.path.isdir(path)
				file_data.append((name, file_type))
		else:
			root_directory_name = sub_path.split("/")[0]
			root_directory_path = g.permission.get_root_directory_path(root_directory_name)
			current_path = root_directory_path + sub_path[len(root_directory_name):]
			if os.path.exists(current_path) and os.path.isdir(current_path):
				if g.permission.has_permission(current_path, g.user.id, g.user.admin):
					files = os.listdir(current_path)
				for file in files:
					file_type = os.path.isdir(os.path.join(current_path, file))
					if file_type:
						if g.permission.has_permission(os.path.join(current_path, file), g.user.id, g.user.admin):
							file_data.append((file, file_type))
					else:
						file_data.append((file, file_type))

	return render_template("index.html", files=file_data, base_url=sub_path)
