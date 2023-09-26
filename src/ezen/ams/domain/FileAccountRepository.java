package ezen.ams.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일를 통해 은행계좌 목록 저장 및 관리(검색, 수정, 삭제) 구현체 (2023-06-13)
 * ObjectOutputStream, ObjectInputStream이용
 * 
 * @author 이희영
 */
public class FileAccountRepository implements AccountRepository {

	private static final String FILE_PATH = "accounts.dbf";

	private List<Account> accounts;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	private int accountNum = 1000;

	@SuppressWarnings("unchecked")
	public FileAccountRepository() throws Exception {
		File file = new File(FILE_PATH);
		// 기존에 파일이 없는 경우..
		if (!file.exists()) {
			accounts = new ArrayList<Account>();
		} else {
			// 기존 파일이 존재할 경우..
			in = new ObjectInputStream(new FileInputStream(file));
			accounts = (List<Account>) in.readObject();
			accountNum = in.readInt();
			in.close();
		}
	}

	/**
	 * 전체 계좌 목록 수 반환
	 * 
	 * @return 목록수
	 */
	public int getCount() {
		return accounts.size();
	}

	/**
	 * 전체 계좌 목록 조회
	 * 
	 * @return 전체계좌 목록
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	/**
	 * 신규계좌 등록
	 * 
	 * @param account 신규계좌
	 * @return 등록 여부
	 */
	public boolean addAccount(Account account) {
		account.setAccountNum(accountNum + "");
		accounts.add(account);
		accountNum++;
		saveFile();
		return true;
	}

	public void saveFile() {
		try {
			out = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
			out.writeObject(accounts);
			out.writeInt(accountNum);
			out.flush();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 계좌번호로 계좌 검색 기능
	 * 
	 * @param accountNum 검색 계좌번호
	 * @return 검색된 계좌
	 */
	public Account searchAccount(String accountNum) {
		for (Account account : accounts) {
			if (account.getAccountNum().equals(accountNum)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * 예금주명으로 계좌 검색 기능
	 * 
	 * @param accountOwner 검색 예금주명
	 * @return 검색된 계좌목록
	 */
	public List<Account> searchAccountByOwner(String accountOwner) {
		List<Account> searchAccounts = new ArrayList<>();
		for (Account account : accounts) {
			if (account.getAccountOwner().equals(accountOwner)) {
				searchAccounts.add(account);
			}
		}
		return searchAccounts;
	}

	/**
	 * 계좌번호로 계좌 삭제 기능
	 * 
	 * @param accountOwner 검색 계좌번호
	 * @return 계좌 삭제 여부
	 */
	public boolean removeAccount(String accountNum) {
		for (int i = 0; i < accounts.size(); i++) {
			Account account = accounts.get(i);
			if (account.getAccountNum().equals(accountNum)) {
				accounts.remove(i);
				saveFile();
				return true;
			}
		}
		return false;
	}

	public void close() {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	// 테스트용 main
//	public static void main(String[] args) throws Exception {
//
//		AccountRepository accountRepository = new FileAccountRepository();
//		List<Account> list = null;
//		Account account = null;
//
//		// 계좌 등록 테스트
//		System.out.println("########## 계좌 등록 테스트  ##########");
//		account = new Account("이희영", 1111, 10000);
//		accountRepository.addAccount(account);
//		account = new MinusAccount("이대출", 1111, 0, 100000);
//		accountRepository.addAccount(account);
//		account = new Account("이희영", 1111, 70000);
//		accountRepository.addAccount(account);
//
//		System.out.println("계좌 등록 완료");
//
//		// 전체 계좌 조회 테스트
//		list = accountRepository.getAccounts();
//		System.out.println("########## 전체 계좌 조회 테스트 ##########");
//		for (Account account2 : list) {
//			System.out.println(account2);
//		}

//		// 계좌번호 검색 테스트
//		account = accountRepository.searchAccount("1000");
//		System.out.println("########## 계좌번호 검색 테스트 ##########");
//		System.out.println(account);
//				
//		// 예금주명 검색 테스트
//		List<Account> searchList = accountRepository.searchAccountByOwner("이희영");
//		System.out.println("########## 예금주명 검색 테스트 ##########");
//		for (Account searchAccount : searchList) {
//			System.out.println(searchAccount);
//		}
//				
//		// 계좌번호 삭제 테스트
//		accountRepository.removeAccount("1002");
//		System.out.println("########## 계좌번호 삭제 테스트 ##########");
//		list = accountRepository.getAccounts();
//		for (Account account2 : list) {
//			System.out.println(account2);
//		}
//	}
}