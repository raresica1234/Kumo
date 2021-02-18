const COOKIE_LOCATION_NAME = "location";

function getCookieLocation() {
	let cookies = decodeURIComponent(document.cookie).split(";");

	let currentPath = "";

	for (let i = 0; i < cookies.length; i++) {
		let cookie = cookies[i];
		if (cookie.startsWith(" "))
			cookie = cookie.substr(1);
		if (cookie.startsWith(COOKIE_LOCATION_NAME + "="))
			currentPath = cookie.substr(COOKIE_LOCATION_NAME.length + 1);
	}

	return currentPath;
}

function clearCookie() {
	document.cookie = COOKIE_LOCATION_NAME + "=; Secure";
}

function explore(location) {
	document.cookie = COOKIE_LOCATION_NAME + "=" + location + "; Secure";
	fetch('explore/' + location)
		.then(res => res.json())
		.then(data => process_request(data));
}

function process_request(data) {
	let currentPath = getCookieLocation();
	if (currentPath !== "")
		currentPath += "/";

	let content = "";

	for (let i = 0; i < data.length; i++)
		content += "<h1><a href='javascript:explore(\"" + currentPath + data[i][0] + "\")'>" + data[i][0] + ", Type: " + (data[i][1] ? "directory" : "file") + "</a></h1>";

	document.getElementById("explorer").innerHTML = content;
}

let previousLocation = getCookieLocation()

fetch(decodeURIComponent(document.URL + "explore/" + previousLocation))
	.then(res => res.json())
	.then(data => process_request(data));
