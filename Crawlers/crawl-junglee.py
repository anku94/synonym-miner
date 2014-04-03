import bs4
import urllib2

__URL = "http://www.junglee.com/s/ref=sr_pg_2?rh=n%3A683853031%2Cn%3A%21683854031%2Cn%3A3575788031%2Cn%3A1914549031&page=1&ie=UTF8&qid=1395868497"
#TEMP_URL = "http://www.junglee.com/s/ref=sr_pg_2?rh=n%3A683853031%2Cn%3A%21683854031%2Cn%3A3575788031%2Cn%3A1914549031&page=%d&ie=UTF8&qid=1395868497"
#TEMP_URL = "http://www.junglee.com/s/ref=sr_pg_2?rh=n%3A683853031%2Cn%3A%21683854031%2Cn%3A3575788031%2Cn%3A1914549031&page="
#TEMP_URL = "http://www.junglee.com/s/ref=sr_pg_2?rh=n%3A683840031%2Cp_20%3AEnglish&page="
TEMP_URL = sys.argv[2]
print TEMP_URL

def appendResult(res):
    f = open('junglee-products.txt', 'a')
    for i in res:
        try:
            i.encode('ascii', 'ignore')
            f.write(i + '\n')
        except UnicodeEncodeError:
            print i
    f.close()

def get_junglee(url):
    results = []
    text = urllib2.urlopen(url).read()
    soup = bs4.BeautifulSoup(text)
    x = soup.findAll('div', attrs = {'class': 'result'})
    for i in x:
        #print i
        t = i.findAll('a', attrs = {'class': 'title'})
        if len(t) > 0:
            print t[0].text
            results.append(t[0].text)
    return results

def run():
    for i in range(1, 200):
        print "Running on " + str(i)
        x = get_junglee(TEMP_URL + str(i))
        if len(x) > 0:
            appendResult(x)
        else:
            return
    return

if __name__ == '__main__':
    run()
