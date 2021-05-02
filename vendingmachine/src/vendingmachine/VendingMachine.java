package vendingmachine.src.vendingmachine;

import java.util.List;

import vendingmachine.src.coin.Coin;
import vendingmachine.src.coin.CoinAssorter;
import vendingmachine.src.coin.CoinType;
import vendingmachine.src.coin.ReturnedCoin;
import vendingmachine.src.product.Product;

public class VendingMachine {
	private ReturnedCoin returnedCoin = new ReturnedCoin();
	private DisplayPanel displayPanel = new DisplayPanel();
	private Product selectedProduct;
	private SavedCoin savedCoin = new SavedCoin();
	private InsertedCoin insertedCoin = new InsertedCoin();

	public List<Coin> returnedCoin() {
		return returnedCoin.coinList();
	}

	public List<Coin> savedCoin() {
		return savedCoin.savedCoinList();
	}

	public String display() {
		return displayPanel.display(insertedCoin.totalAmount());
	}

	public void insertedCoin(Coin coin) {
		if (coin.isValidCoin()) {
			insertedCoin.add(coin);
			savedCoin.add(coin);
			displayPanel.add(coin);
		}
		if (coin.isUnvalidCoin()) {
			returnedCoin.add(coin);
		}
	}

	public void pushButton(Product product) {
		this.selectedProduct = product;
	}

	public void check() {
		displayPanel.check();
	}

	public Product returnedProduct() {
		Product returnedProduct = null;
		if (insertedCoin.totalAmount() >= selectedProduct.amount()) {
			returnedProduct = selectedProduct;
			settleCharge();
			clearSelectedProduct();
			insertedCoin.clear();
			displayPanel.clear();
		}
		return returnedProduct;
	}

	private void settleCharge() {
		int totalReturnedCoinAmount = insertedCoin.totalAmount() - selectedProduct.amount();
		CoinType returnedCoinType = CoinAssorter.checkCoin(totalReturnedCoinAmount);
		Coin returnedCoin = CoinAssorter.createCoin(returnedCoinType);
		this.returnedCoin.add(returnedCoin);
	}

	private void clearSelectedProduct() {
		selectedProduct = null;
	}

}
