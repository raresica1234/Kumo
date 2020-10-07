# Kumo
Kumo is a cloud
## Dependencies
- [Flask](https://flask.palletsprojects.com/en/1.1.x/ "Flask")
- [Flask-SQLAlchemy](https://flask-sqlalchemy.palletsprojects.com/en/2.x/ "Flask SQLAlchemy")

## Setting up
To set up the database you need to do `flask init-db`. Then you can simply run the project: `flask run`.

### Configuration
In the folder `instance` create a file called `config.json` this file will contain all the root media directories you want the cloud to handle.
**Note:** The media paths must be absolute paths.

```json
{
	"media": [
		"example_dir",
		"example_dir2"
	]
}
```

`file_logging` represents whether the logging should happen in files or be displayed in the console.
Once you set the media directories, in order to update them run `flask update-media`.

### Runtime change through environment variables

|Variable|Value|Description|
|----|-----|----|
|`KUMO_FILE_LOGGING`|0 or 1|if set to 1 it forces kumo to print all output to file instead of console|
