import click
from flask.cli import with_appcontext
from flask import current_app, g
from kumo.src.util import config
from kumo.src.model.media_directory import db, MediaDirectory


def pick_non_empty_directory_name(dirname: str):
	if dirname.startswith("/"):
		if dirname.endswith("/"):
			dirname = dirname[:-1]
		return dirname.split("/")[-1]
	else:
		return dirname[:-1]


def resolve_path(sub_path: str):
	root_directory_name = sub_path.split("/")[0]
	root_directory_path = g.permission.get_root_directory_path(root_directory_name)
	return root_directory_path + sub_path[len(root_directory_name):]


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
			new_media_directory = MediaDirectory(name=pick_non_empty_directory_name(directory), path=directory,
												 root=True)
			db.session.add(new_media_directory)

	db.session.commit()
