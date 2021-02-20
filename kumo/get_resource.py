import os
from kumo.unix_permissions import UnixPermissions
from flask import Blueprint, jsonify, g, render_template, redirect, url_for, send_from_directory, abort

bp = Blueprint("img", __name__)


@bp.route("/img/")
@bp.route("/img")
@bp.route("/img/<path:sub_path>")
def img(sub_path=None):
	if "permission" not in g:
		g.permission = UnixPermissions()

	if g.user is not None:
		if sub_path is not None:
			root_directory_name = sub_path.split("/")[0]
			root_directory_path = g.permission.get_root_directory_path(root_directory_name)
			current_path = root_directory_path + sub_path[len(root_directory_name) + 1:]
			print(current_path)
			filename = current_path.split("/")[-1]
			directory = current_path[0:len(current_path) - len(filename)]
			print(filename, directory)
			if g.permission.has_permission(current_path, g.user.id, g.user.admin):
				print("a")
				return send_from_directory(directory, filename=filename)

	abort(404)
