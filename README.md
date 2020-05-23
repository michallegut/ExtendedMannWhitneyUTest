The orginal verion of ```MannWhitneyUTest``` class from Apache Commons Math has two public methods:

* ```mannWhitneyU(double[] x, double[] y)``` which returns only U statistic of the test and the wrong one (greater instead of lesser)
* ```mannWhitneyUTest(double[] x, double[] y)``` which returns only p-value of the test

This extended version was modified to have only one public method ```mannWhitneyUTest(double[] x, double[] y)``` which returns a ```Map<String, Double>``` with every possible statistic of the test:

Key | Statistic
------------ | -------------
"n1" | number of observations in x sample
"n2" | number of observations in y sample
"U1" | U statistic for x sample
"U2" | U statistic for y sample
"R1" | sum of ranks for x sample
"R2" | sum of ranks for y sample
"meanr1" | mean rank for x sample
"meanr2" | mean rank for y sample
"Umin" | lesser of the U statistics
"p" | p-value of the test