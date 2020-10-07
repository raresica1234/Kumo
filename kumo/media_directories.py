import click
from flask.cli import with_appcontext
from flask import current_app, g
from kumo import config
from kumo.models import MediaDirectory, MediaPermissions, db


def check_read_permission(directory):
    if g.user is None:
        return False
    if g.user.admin == 1:
        return True
    else:
        permissions = MediaPermissions.query.filter_by(user_id=g.user.id).all()
        media_dirs = []
        for permission in permissions:
            if permission.read_permission:
                media_dirs.append(MediaDirectory.query.filter_by(id=permission.media_id))

        return False


@click.command("update-media")
@with_appcontext
def update_media_command():
    existing_directories = MediaDirectory.query.all()
    directories = config.media_directories

    for directory in directories:
        found = False
        for existing_directory in existing_directories:
            if directory == existing_directory.path:
                found = True
                break

        if not found:
            current_app.logger.info("Found new media directory" + directory)
            new_media_directory = MediaDirectory(name=directory.split("/")[-1], path=directory)
            db.session.add(new_media_directory)

    db.session.commit()

