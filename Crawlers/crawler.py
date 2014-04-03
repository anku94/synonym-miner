import bs4
import urllib2
import os
rows, columns = os.popen('stty size', 'r').read().split()

def getName(url):
    data = urllib2.urlopen(url).read()
    b = bs4.BeautifulSoup(data)
    d = b.findAll('h1', attrs = {'itemprop':'name'})
    if(len(d) > 0):
        return d[0].text
    else:
        return ""

def progress(curr, target):
    per = curr * 1.0/target
    l = float(columns) * per
    l = l - 15
    if l < 0:
        l = 0

    t = float(columns) - 15.0;
    l = int(l)
    t = int(t)
    per = int(per*100)
    s = '=' * l + '>' + ' ' * (t - l)
    fs = '  [ %s ] %d'%(s, per)
    fs += "%"
    print "\r" + fs,
def download(fname):
    f = open(fname).read().split('\n')
    f = [i for i in f if len(i) > 5]
    l = len(f)
    resp = []
    for i, j in enumerate(f):
        try:
            if(j.startswith('*')):
                pass
            else:
                progress(i, l)
                x = getName(j)
                print x
                resp.append(x)
        except KeyboardInterrupt:
            ff = open('names-python', 'a')
            for p in resp:
                ff.write(p + '\n')
            ff.close()

            g = open(fname, 'w')

            for k in range(l):
                if k < i - 1:
                    g.write('*' + f[k] + '\n')
                else:
                    g.write(f[k] + '\n')
            g.close()
            raise
        except Exception as e:
            print e
            ff = open('names-python', 'a')
            for p in resp:
                ff.write(p + '\n')
            ff.close()

            g = open(fname, 'w')

            for k in range(l):
                if k < i - 1:
                    g.write('*' + f[k] + '\n')
                else:
                    g.write(f[k] + '\n')
            g.close()
            return

if __name__ == '__main__':
    download('product_links.txt')
    pass
