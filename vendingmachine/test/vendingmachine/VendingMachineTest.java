package vendingmachine.test.vendingmachine;

import static org.junit.jupiter.api.Assertions.*;
import static vendingmachine.src.product.Product.*;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import vendingmachine.src.coin.Coin;
import vendingmachine.src.coin.CoinAssorter;
import vendingmachine.src.coin.CoinCreater;
import vendingmachine.src.coin.CoinType;
import vendingmachine.src.product.Product;
import vendingmachine.src.vendingmachine.VendingMachine;

class VendingMachineTest {
	private VendingMachine vendingMachine = new VendingMachine();
	private Coin unValidCoin = CoinCreater.createUnValidCoin();
	private Coin nickel = CoinCreater.createNickel();
	private Coin dime = CoinCreater.createDime();
	private Coin quarter = CoinCreater.createQuarter();

	@Nested
	class 自動販売機の画面表示が正しく表示されること {
		@Test
		void コインを一枚も入れないとINSERTCOINと表示されること() {
			assertEquals("INSERTCOIN", vendingMachine.display());
		}

		@Test
		void ダイムを1枚入れるとコインの合計金額が0_1ドルと表示されること() {
			vendingMachine.insertedCoin(dime);
			assertEquals("0.1", vendingMachine.display());
		}

		@Test
		void ニッケルを1枚入れるとコインの合計金額が0_05ドルと表示されること() {
			vendingMachine.insertedCoin(nickel);
			assertEquals("0.05", vendingMachine.display());
		}

		@Test
		void クオーターを1枚入れるとコインの合計金額が0_25ドルと表示されること() {
			vendingMachine.insertedCoin(quarter);
			assertEquals("0.25", vendingMachine.display());
		}

		@Test
		void ニッケルとダイムとクオーターを1枚ずつ入れるとコインの合計金額が0_4ドルと表示されること() {
			vendingMachine.insertedCoin(dime);
			vendingMachine.insertedCoin(nickel);
			vendingMachine.insertedCoin(quarter);
			assertEquals("0.4", vendingMachine.display());
		}

		@Test
		void プロダクト購入後にTHANKYOUが表示されること() {
			vendingMachine.pushButton(CHIPS);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.returnedProduct();
			String messageWhenAfterCheck = vendingMachine.display();
			assertEquals("THANKYOU", messageWhenAfterCheck);
		}
	}

	@Nested
	class 自動販売機に入れた無効なコインが返ってくること {
		@Test
		void コインを一枚も入れないとreturnedCoinには何も入っていないこと() {
			assertTrue(vendingMachine.returnedCoin().size() == 0);
		}

		@Test
		void ダイムを1枚入れるとreturnedCoinには何も入っていないこと() {
			vendingMachine.insertedCoin(dime);
			assertTrue(vendingMachine.returnedCoin().size() == 0);
		}

		@Test
		void ニッケルを1枚入れるとreturnedCoinには何も入っていないこと() {
			vendingMachine.insertedCoin(nickel);
			assertTrue(vendingMachine.returnedCoin().size() == 0);
		}

		@Test
		void クオーターを1枚入れるとreturnedCoinには何も入っていないこと() {
			vendingMachine.insertedCoin(quarter);
			assertTrue(vendingMachine.returnedCoin().size() == 0);
		}

		@Test
		void 定義されていないコインを1枚入れるとreturnedCoinに返却されること() {
			vendingMachine.insertedCoin(unValidCoin);
			assertEquals(unValidCoin, vendingMachine.returnedCoin().get(0));
		}
	}

	@Nested
	class 自動販売機に入れた有効なコインが貯蔵されること {
		@Test
		void コインを一枚も入れないとsavedCoinには何も入っていないこと() {
			assertTrue(vendingMachine.savedCoin().size() == 0);
		}

		@Test
		void ダイムを1枚入れるとsavedCoinにはダイムが入っていること() {
			vendingMachine.insertedCoin(dime);
			assertEquals(vendingMachine.savedCoin().get(0), dime);
		}

		@Test
		void ニッケルを1枚入れるとsavedCoinにはニッケルが入っていること() {
			vendingMachine.insertedCoin(nickel);
			assertEquals(vendingMachine.savedCoin().get(0), nickel);
		}

		@Test
		void クオーターを1枚入れるとsavedCoinにはクオーターが入っていること() {
			vendingMachine.insertedCoin(quarter);
			assertEquals(vendingMachine.savedCoin().get(0), quarter);
		}

		@Test
		void 定義されていないコインを1枚入れるとsavedCoinには何も入っていないこと() {
			vendingMachine.insertedCoin(unValidCoin);
			assertTrue(vendingMachine.savedCoin().size() == 0);
		}
	}

	@Nested
	class 商品よりも高い金額を入れるとお釣りが返ってくること {
		@Test
		void _75セント入れて65セントの商品を買うと10セント返ってくること() {
			Coin quarter = CoinCreater.createQuarter();
			Coin dime = CoinCreater.createDime();
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.pushButton(CANDY);
			vendingMachine.returnedProduct();
			assertEquals(CoinAssorter.checkCoin(dime), CoinAssorter.checkCoin(vendingMachine.returnedCoin().get(0)));
		}
	}

	@Nested
	class ボタンを押して選択したプロダクトが一定金額を入れれば買えること {
		@Test
		void コーラが1ドルで購入できること() {
			vendingMachine.pushButton(COLA);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			Product returnedProduct = vendingMachine.returnedProduct();
			assertEquals(COLA, returnedProduct);
		}

		@Test
		void キャンディが65セントで購入できること() {
			vendingMachine.pushButton(CANDY);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(dime);
			vendingMachine.insertedCoin(nickel);
			Product returnedProduct = vendingMachine.returnedProduct();
			assertEquals(CANDY, returnedProduct);
		}

		@Test
		void チップスが50セントで購入できること() {
			vendingMachine.pushButton(CHIPS);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			Product returnedProduct = vendingMachine.returnedProduct();
			assertEquals(CHIPS, returnedProduct);
		}

		@Test
		void コーラが75セントでは購入できないこと() {
			vendingMachine.pushButton(COLA);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			Product returnedProduct = vendingMachine.returnedProduct();
			assertNull(returnedProduct);
		}
	};

	@Nested
	class 返却ボタンを押すと投入したコインが返ってくること {
		@Test
		void _65セント入れた状態で返却ボタンを押すと65セントが返ってくること() {
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(quarter);
			vendingMachine.insertedCoin(dime);
			vendingMachine.insertedCoin(nickel);
			vendingMachine.pushReturnedButton();
			List<Coin> returnedCoinList = vendingMachine.returnedCoin();
			int returnedCoinAmount = 0;
			for (Coin coin : returnedCoinList) {
				CoinType coinType = CoinAssorter.checkCoin(coin);
				returnedCoinAmount += coinType.value();
			}
			assertEquals(returnedCoinAmount, 65);
		}
	}

}
