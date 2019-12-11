*** Online Collaboration ***
1. Features
	- login/register
	- upload files
	- download files
	- delete files
	- share files with other users
	- edit same file simultaniously by multiple users
	- one user can edit multiple files simultaniously
	- both the creator and the user with which the file was shared can delete the share
	- only. txt file extension is supported for now
2. Future goals
	- add support for doc/docx
	- send notifications to recepients
	- improve GUI of the site
3. Installation
	- clone the repo
	- make sure you have downloaded Apache server (XAMPP is a perfect match for that)
	- move the files into the htdocs folder
	- configure the database, using the sql scripts located in sql directory. setup.sql creates the database and the tables
	  and insert_data.sql adds the needed data so that the system can function properly. You can connect to the database either threw the
	  terminal or using the database control panel (e.g. PHPMyAdmin of XAMPP). If the host, name and possword used for connecting to the
	  database are different than the ones in config.ini in the config folder, then you need to reconfigure them.
	- now use either Chrome, Firefox or Opera to connect to localhost.
