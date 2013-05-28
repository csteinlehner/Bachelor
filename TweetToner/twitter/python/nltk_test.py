from pymongo import Connection
from pymongo import database
import datetime
import nltk
from nltk.collocations import *
from nltk.probability import FreqDist
from pytz import timezone
import pytz


city = 'berlin'

bigram_measures = nltk.collocations.BigramAssocMeasures()
trigram_measures = nltk.collocations.TrigramAssocMeasures()

#### MongoDB connect
connection = Connection('mongodb://norrix_mongoadmin:EntIftub@localhost:20707')
db = database.Database(connection, 'datasift')
# db.authenticate('norrix_mongoadmin', 'EntIftub')
col = db[city]

time_dict = {"berlin": "Europe/Berlin", "potsdam": "Europe/Berlin", "munchen": "Europe/Berlin", "rosenheim": "Europe/Berlin", "newyork": "US/Eastern", "sanfrancisco": "US/Pacific", "menlopark": "US/Pacific", "curpertino": "US/Pacific"}

fmt = '%Y-%m-%d %H:%M:%S %Z%z'

tz = timezone(time_dict[city])
start_range = datetime.datetime(2012, 9, 3, 0, 0, 0, tzinfo=pytz.utc)
end_range = datetime.datetime(2012, 9, 9, 23, 55, 0, tzinfo=pytz.utc)

start_range = start_range.astimezone(tz)
end_range = end_range.astimezone(tz)

t_start = start_range

# widgets = ['Something: ', Percentage(), ' ', Bar(marker=RotatingMarker()),
#         ' ', ETA(), ' ', FileTransferSpeed()]
# pbar = ProgressBar(widgets=widgets, maxval=24 * 7).start()
while (t_start < end_range):
    t_end = t_start + datetime.timedelta(hours=24)
    content_agg = ''
    # .
    for row in col.find({'fsq_categorieParents': {'$exists': False}, 'deleted': {'$exists': False}, 'ignore': {'$exists': False}, "created_at_dt": {"$gt": t_start, "$lt": t_end}}):
        content_agg += (row[u'interaction'][u'content'] + " ").encode("utf-8")

    # print content_agg.__len__()
    if (content_agg.__len__() > 0):
        content_agg = nltk.wordpunct_tokenize(content_agg)
        content_agg = [w for w in content_agg if not w in nltk.corpus.stopwords.words('twitter')]
        content_agg = [w for w in content_agg if not w in nltk.corpus.stopwords.words('german')]
        content_agg = [w for w in content_agg if not w in nltk.corpus.stopwords.words('english')]

        finder = BigramCollocationFinder.from_words(
           content_agg)

        # finder.apply_freq_filter(3)
        finder.apply_word_filter(lambda w: len(w) < 4)
        # fd = FreqDist([w for w in content_agg if len(w) > 3])
        # print fd
        print t_start.strftime(fmt)
        # print finder.nbest(bigram_measures.pmi, 3)
        print finder.score_ngrams(bigram_measures.pmi)
        print '\n'
    t_start = t_end
