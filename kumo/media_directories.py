import click
from os import path
from flask.cli import with_appcontext
from flask import current_app
from kumo import config
from kumo.models import MediaDirectory, db


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

