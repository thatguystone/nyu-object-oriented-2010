class caseChaining {
	public void test() {
		test2().test2().test2();
	}
	
	public caseChaining test2() {
		return this;
	}
}
