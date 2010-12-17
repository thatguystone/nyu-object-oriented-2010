import glob
import os
import subprocess
import output
import threading
import thread

ERROR = 1
OK = 2
TESTDIR = os.getcwd()

#the list of test cases
cases = []

#and since threads screw with each other's printing, we need to lock print
printLock = threading.Semaphore()

#a container for the outputs of the tests
class runTest(object):
	pass

def main():
	global cases
	cases = findCases()
	
	results = []
	threads = []
	
	os.chdir(TESTDIR)
	
	#start up 4 threads to do our testing
	for i in range(4):
		t = threading.Thread(target=runCases, args=(results, ))
		t.start()
		threads.append(t)
	
	#wait for all the threads to finish their stuff
	for t in threads:
		t.join()
	
	printResults(sorted(results, key=lambda res: res[0]))

def p(*s):
	printLock.acquire()
	print " ".join(s)
	printLock.release()

def printResults(res):
	padding = 60
	print output.bold("\n%s %60s" % ("File", "Result"))
	
	for r in res:
		print r[0] + " " * (padding - len(r[0]) - 1) + r[1]

def getCase():
	global cases
	if (len(cases) == 0):
		return False
		
	#lists are synchronized :-D
	return cases.pop()

def runCases(results):
	while True:
		case = getCase()
		if (case == False):
			break
		runFile(results, case)
	
	#remove the temporary files we created
	name = getCppName()
	os.remove(TESTDIR + "/../" + name)
	os.remove(TESTDIR + "/../" + name.replace(".cpp", ".h"))
	os.remove(TESTDIR + "/../" + name.replace(".cpp", ".h.gch"))
	os.remove(TESTDIR + "/../" + name.replace(".cpp", ".out"))
	
def findCases():
	results = []
	#go through all of our directories in the test folder
	for d in os.walk(os.getcwd()):
		os.chdir(d[0])
		tests = glob.glob("*.java")
		
		currDir = os.getcwd().replace(TESTDIR, "") + "/"
		
		#run the translator C++ output against the java output
		for t in tests:
			results.append(dict(
				chdir = d[0],
				f = currDir[1:] + t
			))
	
	return results

def runFile(results, info):
	#extract our information
	f = info["f"]
	
	#check to see if the file has a main method...otherwise, no test to run there
	tmpFile = open(f, "r")
	hasMain = tmpFile.read().find("main(String[] args)")
	tmpFile.close()
	if (hasMain == -1):
		p("Ignoring file without main method:", f)
		return
	
	#print a status so that the user feels better
	p("Running test:", f)
	
	#our container for the outputs of the test
	test = runTest()
	
	#test the java
	ret = compileJavaFile(f, test)
	if (ret[0] != OK):
		results.append((f, output.darkred(ret[1])))
		return
	
	runJavaFile(f, test)
	
	#test the C++
	ret = translateJava(f, test)
	if (ret[0] != OK):
		results.append((f, output.darkred(ret[1])))
		return
	
	ret = compileCpp(f, test)
	if (ret[0] != OK):
		results.append((f, output.darkred(ret[1])))
		return
	
	runCpp(test)
	
	#once we get here, check if our outputs match
	if (test.javaRun[0] != test.cppRun[0]):
		results.append((f, output.red("FAIL")))
	elif (test.javaRun[1] != test.cppRun[1]):
		results.append((f, output.yellow("Different stderr")))
	elif (test.javaExitCode != test.cppExitCode):
		results.append((f, output.yellow("Different exit codes")))
	else:
		results.append((f, output.green("PASS")))

def compileJavaFile(f, test):
	proc = subprocess.Popen(["./make", "compileTestFile", "file=" + f.replace(".java", ".class")], cwd = TESTDIR + "/..", stdout = subprocess.PIPE, stderr = subprocess.PIPE)
	ret = proc.communicate()
	
	if (proc.returncode != 0 or len(ret[1]) != 0):
		return (ERROR, "Javac Compile Error")
	
	return (OK, )

def runJavaFile(f, test):
	proc = subprocess.Popen(["java", f.replace(".java", "")], stdout = subprocess.PIPE, stderr = subprocess.PIPE)
	ret = proc.communicate()
	
	test.javaRun = ret
	test.javaExitCode = proc.returncode

def translateJava(f, test):
	proc = subprocess.Popen(
		["./make", "run", "file=" + f.replace(".java", ""), "outputFile=" + getCppName()], 
		cwd = TESTDIR + "/..", stdout = subprocess.PIPE, stderr = subprocess.PIPE
	)
	ret = proc.communicate()
	
	if (proc.returncode != 0 or len(ret[1]) != 0):
		return (ERROR, "Translate Error")
	
	return (OK, )

def compileCpp(f, test):
	proc = subprocess.Popen(["./make", "cpp_compile", "outputFile=" + getCppName()], cwd = TESTDIR + "/..", stdout = subprocess.PIPE, stderr = subprocess.PIPE)
	ret = proc.communicate()
	
	if (proc.returncode != 0 or len(ret[1]) != 0):
		return (ERROR, "g++ Compile Error")
	
	return (OK, )

def runCpp(test):
	proc = subprocess.Popen([TESTDIR + "/../" + str(thread.get_ident()) + ".out"], stdout = subprocess.PIPE, stderr = subprocess.PIPE)
	ret = proc.communicate()
	
	test.cppRun = ret
	test.cppExitCode = proc.returncode

def getCppName():
	return str(thread.get_ident()) + ".cpp"

if __name__ == "__main__":
	main()
