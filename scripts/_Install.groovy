ant.mkdir(dir: "${basedir}/test/cucumber")
ant.mkdir(dir: "${basedir}/test/cucumber/json")

println '''
**********************************************************
* You've installed the cucumber JSON plugin.             *
*                                                        *
* please run jsonSetup to configure steps and env.groovy *
*                                                        *
**********************************************************
'''