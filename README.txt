

Our codebase consists of three components:

1. Crawlers (used to generate dataset)

The functionality of the crawlers differs according to the website they crawl. For example - Flipkart retrieves products using AJAX, so we had to use a Javascript crawler.

For Junglee, Amazon and IMDb, we wrote crawlers in Python.

All the data we crawled is stored in Crawlers/flipkart-dataset, Crawlers/imdbCrawl and Crawlers/junglee-crawl

2. Backend Code:

	Dependencies:

	1) gson-2.2.4.jar

	2) jsoup-1.7.3.jar


	How to run?

	1) Compile the java files along with above jar files.

	2) The code uses "proxy.iiit.ac.in:8080" i.e. is set to run on IIIT Intranet.

	3) Make sure that internet is connected before running the code.

	4) Run the class "TestGoogleSea" as -> java TestGoogleSea "path_of_config_file_with_name"

	5) Config file contains first line as temporary folder path where the cached valus are stored. Initially this folder is empty.

   	And second line contains product database. i.e. full product names which are being used for synonym recognition.

	5) Give the query as input on system standard input.


3. Frontend

The Frontend code is located in Webapp/. The interface is ire.html. It makes AJAX calls to process.php with the query. process.php in turn, calls query.sh with the appropriate query.

query.sh runs the JAR file, and stores its output in a text file. We update the contents of the text file via AJAX calls.

In order to redeploy it, the paths in query.sh and ire-js.js will have to be changed to correspond to the new locations of configuration and JAR files.
