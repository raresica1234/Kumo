import os
import kumo.media_directories as media
from flask import Blueprint, jsonify

bp = Blueprint("explorer", __name__)


@bp.route("/explore")
@bp.route("/explore/<path:subpath>")
def explore(subpath=None):
    file_data = []
    files = []
    if subpath is None:
        files = media.get_readable_directories()
        for file in files:
            file_type = os.path.isdir(file)
            file_data.append((file, file_type))
    else:
        current_path = "/" + subpath
        if media.check_read_permission(current_path):
            files = os.listdir(current_path)
        for file in files:
            file_type = os.path.isdir(os.path.join(current_path, file))
            file_data.append((file, file_type))

    return jsonify(file_data)
