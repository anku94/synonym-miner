Our codebase consists of three components:

1. Crawlers (used to generate dataset)

The functionality of the crawlers differs according to the website they crawl. For example - Flipkart retrieves products using AJAX, so we had to use a Javascript crawler.

For Junglee, Amazon and IMDb, we wrote crawlers in Python.

All the data we crawled is stored in Crawlers/flipkart-dataset, Crawlers/imdbCrawl and Crawlers/junglee-crawl

2. Backend Code

<Please update this section>

3. Frontend

The Frontend code is located in Webapp/. The interface is ire.html. It makes AJAX calls to process.php with the query. process.php in turn, calls query.sh with the appropriate query.

query.sh runs the JAR file, and stores its output in a text file. We update the contents of the text file via AJAX calls.

In order to redeploy it, the paths in query.sh and ire-js.js will have to be changed to correspond to the new locations of configuration and JAR files.
