import os
from kumo.src.permission.unix_permissions import UnixPermissions
from flask import Blueprint, g, render_template, redirect, url_for, request
from kumo.src.util.media_directories import resolve_path

bp = Blueprint("explorer", __name__)

ITEMS_PER_PAGE = 21


def is_image(path: str):
	extension = path.split(".")[-1]
	extension = extension.lower()
	if extension == "png" or extension == "jpg" or extension == "jpeg":
		return True
	return False


@bp.route("/explore")
@bp.route("/explore/")
@bp.route("/explore/<path:sub_path>")
def explore(sub_path=None):
	if g.user is None:
		return redirect(url_for("auth.login"))

	if "permission" not in g:
		g.permission = UnixPermissions()

	current_page = request.args.get('page')

	file_data = []
	files = []
	if g.user is not None:
		if sub_path is None:
			directories = g.permission.get_readable_root_directories(g.user.id, g.user.admin)
			for name, path in directories:
				file_type = os.path.isdir(path)
				file_data.append((name, file_type, is_image(path)))
		else:
			current_path = resolve_path(sub_path)
			if os.path.exists(current_path) and os.path.isdir(current_path):
				if g.permission.has_permission(current_path, g.user.id, g.user.admin):
					files = os.listdir(current_path)
				for file in files:
					file_type = os.path.isdir(os.path.join(current_path, file))
					if file_type:
						if g.permission.has_permission(os.path.join(current_path, file), g.user.id, g.user.admin):
							file_data.append((file, file_type, is_image(file)))
					else:
						file_data.append((file, file_type, is_image(file)))

	if current_page is None:
		file_data = file_data[0:ITEMS_PER_PAGE]
	else:
		try:
			page = int(current_page)
			if ITEMS_PER_PAGE * page > len(file_data):
				file_data = []
			else:
				file_data = file_data[ITEMS_PER_PAGE * (page - 1):ITEMS_PER_PAGE * page]
		except ValueError:
			file_data = file_data[0:ITEMS_PER_PAGE]

	return render_template("index.html", files=file_data, base_url=sub_path)
