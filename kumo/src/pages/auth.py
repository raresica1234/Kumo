import functools

from flask import Blueprint, flash, g, redirect, render_template, request, session, url_for
from kumo.src.model.user import db, User

from werkzeug.security import check_password_hash, generate_password_hash

bp = Blueprint("auth", __name__, url_prefix="/auth")


@bp.route("/register", methods=("GET", "POST"))
def register():
	if request.method == 'POST':
		username = request.form["username"]
		password = request.form["password"]

		error = ""
		if not username:
			error += "Username is required.\n"
		if not password:
			error += "Password is required.\n"

		if error == "":
			if User.query.filter_by(username=username).first() is not None:
				error += "User {} is already registered.\n".format(username)

		if error == "":
			user = User(username=username, password=generate_password_hash(password))
			db.session.add(user)
			db.session.commit()

			return redirect(url_for('auth.login'))

		flash(error)

	return render_template('auth/register.html')


@bp.route("/login", methods=("GET", "POST"))
def login():
	if request.method == 'POST':
		username = request.form["username"]
		password = request.form["password"]

		error = ""
		if not username:
			error += "Username is required!\n"
		if not password:
			error += "Password is required!\n"

		if error == "":
			user = User.query.filter_by(username=username).first()
			print(user)
			if user is None:
				error += "Username does not exist.\n"
			elif not check_password_hash(user.password, password):
				error += "Incorrect password. \n"

			if error == "":
				session.clear()
				session["user_id"] = user.id
				return redirect(url_for("index"))

		flash(error)

	return render_template("auth/login.html")


@bp.before_app_request
def load_logged_in_user():
	user_id = session.get("user_id")
	if user_id is None:
		g.user = None
	else:
		g.user = User.query.filter_by(id=user_id).first()


@bp.route("/logout")
def logout():
	session.clear()
	return redirect(url_for("index"))


def login_required(view):
	@functools.wraps(view)
	def wrapped_view(**kwargs):
		if g.user is None:
			return redirect(url_for("auth.login"))

		return view(**kwargs)

	return wrapped_view
