import os
from kumo.unix_permissions import UnixPermissions
from flask import Blueprint, jsonify, g

bp = Blueprint("explorer", __name__)


@bp.route("/explore")
@bp.route("/explore/<path:subpath>")
def explore(subpath=None):
    if "permission" not in g:
        g.permission = UnixPermissions()

    file_data = []
    files = []
    if subpath is None:
        files = g.permission.get_readable_root_directories(g.user.id, g.user.admin)
        for file in files:
            file_type = os.path.isdir(file)
            file_data.append((file, file_type))
    else:
        current_path = "/" + subpath
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

    return jsonify(file_data)
