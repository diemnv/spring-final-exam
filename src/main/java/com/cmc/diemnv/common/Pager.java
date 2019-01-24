package com.cmc.diemnv.common;

public class Pager {
	private int pager[];

	private Pager(int lengthOfSection) {
		pager = new int[lengthOfSection + 2];
	}

	public int[] getPaing(int pageLabelValue, int sizeOfPage, int totalRecord) {
		int lengthOfSection = pager.length - 2;
		int currentSection = (pageLabelValue - 1) / lengthOfSection;
		if (currentSection > 0) {
			pager[0] = (currentSection - 1) * lengthOfSection + 1;
		}
		if ((currentSection + 1) * lengthOfSection * sizeOfPage < totalRecord) {
			pager[pager.length - 1] = (currentSection + 1) * lengthOfSection + 1;
		}

		for (int i = 1; i <= lengthOfSection; i++) {
			if ((currentSection * lengthOfSection + i) * sizeOfPage - totalRecord < sizeOfPage) {
				pager[i] = currentSection * lengthOfSection + i;
			}
		}
		return pager;
	}

	/*
	 * public static void main(String[] args) { Pager pager = new Pager(4); int
	 * array[] = pager.getPaing(5, 3, 20);
	 * System.err.println(Arrays.toString(array)); }
	 */}
