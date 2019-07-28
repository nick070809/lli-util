package org.kx.util;

/**
 * Created by sunkx on 2017/3/28.
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;

public class Money implements Serializable, Comparable<Money> {
    private static final long serialVersionUID = 2084574150755995748L;
    private static final String DEFAULT_CURRENCY_CODE = "CNY";
    private static final int DEFAULT_ROUNDING_MODE = 4;
    private static final BigDecimal THOUSAND = BigDecimal.valueOf(1000L);
    private static final String CNH = "CNH";
    private BigDecimal yuanAmount;
    private String currencyCode;

    public Money() {
        this.currencyCode = "CNY";
        this.yuanAmount = BigDecimal.valueOf(0L, 2);
    }

    public Money(long li) {
        this.currencyCode = "CNY";
        Currency currency = Currency.getInstance("CNY");
        this.yuanAmount = this.getLegalAmountFromLi(currency, li);
    }

    public Money(String yuanAmount) {
        this.currencyCode = "CNY";
        Currency currency = Currency.getInstance("CNY");
        this.yuanAmount = this.getLegalAmountFromStringAmount(currency, yuanAmount);
    }

    public Money(String currencyCode, long li) {
        if("CNH".equals(currencyCode)) {
            this.currencyCode = "CNH";
            BigDecimal currency = new BigDecimal(li);
            this.yuanAmount = currency.divide(THOUSAND, 2, 4);
        } else {
            Currency currency1 = Currency.getInstance(currencyCode);
            this.currencyCode = currency1.getCurrencyCode();
            this.yuanAmount = this.getLegalAmountFromLi(currency1, li);
        }

    }

    public Money(String currencyCode, String yuanAmount) {
        if("CNH".equals(currencyCode)) {
            this.currencyCode = "CNH";
            BigDecimal currency = new BigDecimal(yuanAmount);
            this.yuanAmount = currency.divide(BigDecimal.ONE, 2, 4);
        } else {
            Currency currency1 = Currency.getInstance(currencyCode);
            this.currencyCode = currency1.getCurrencyCode();
            this.yuanAmount = this.getLegalAmountFromStringAmount(currency1, yuanAmount);
        }

    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        boolean currentCurrencyDigits = false;
        int currentCurrencyDigits1;
        if("CNH".equals(this.currencyCode)) {
            currentCurrencyDigits1 = 2;
        } else {
            Currency passInCurrencyDigits = Currency.getInstance(this.currencyCode);
            currentCurrencyDigits1 = passInCurrencyDigits.getDefaultFractionDigits();
        }

        boolean passInCurrencyDigits1 = false;
        int passInCurrencyDigits2;
        if("CNH".equals(currencyCode)) {
            passInCurrencyDigits2 = 2;
        } else {
            Currency passInCurrency = Currency.getInstance(currencyCode);
            passInCurrencyDigits2 = passInCurrency.getDefaultFractionDigits();
        }

        if(currentCurrencyDigits1 == passInCurrencyDigits2) {
            this.currencyCode = currencyCode;
        } else {
            if(passInCurrencyDigits2 >= 4) {
                throw new IllegalArgumentException("Currency  is unsupported: " + currencyCode);
            }

            this.yuanAmount = this.yuanAmount.divide(BigDecimal.ONE, passInCurrencyDigits2, 4);
            this.currencyCode = currencyCode;
        }

    }

    public long getLi() {
        return this.yuanAmount.multiply(THOUSAND).longValue();
    }

    public void setLi(long li) {
        if("CNH".equals(this.currencyCode)) {
            this.yuanAmount = BigDecimal.valueOf(li).divide(THOUSAND, 2, 4);
        } else {
            Currency currentCurrency = Currency.getInstance(this.currencyCode);
            this.yuanAmount = BigDecimal.valueOf(li).divide(THOUSAND, currentCurrency.getDefaultFractionDigits(), 4);
        }

    }

    public String getYuanAmount() {
        return this.yuanAmount.toString();
    }

    public Money add(Money other) {
        this.assertSameCurrencyAs(other);
        BigDecimal amountTmp = this.yuanAmount.add(other.yuanAmount);
        return new Money(this.currencyCode, amountTmp);
    }

    public Money subtract(Money other) {
        this.assertSameCurrencyAs(other);
        BigDecimal amountTmp = this.yuanAmount.subtract(other.yuanAmount);
        return new Money(this.currencyCode, amountTmp);
    }

    public Money multiply(double factor) {
        BigDecimal amountTmp = this.yuanAmount.multiply(BigDecimal.valueOf(factor));
        return new Money(this.currencyCode, amountTmp);
    }

    public Money divide(double factor) {
        BigDecimal amountTmp = null;
        if("CNH".equals(this.currencyCode)) {
            amountTmp = this.yuanAmount.divide(BigDecimal.valueOf(factor), 2, 4);
        } else {
            Currency currency = Currency.getInstance(this.currencyCode);
            amountTmp = this.yuanAmount.divide(BigDecimal.valueOf(factor), currency.getDefaultFractionDigits(), 4);
        }

        return new Money(this.currencyCode, amountTmp);
    }

    public String toString() {
        return this.currencyCode + " " + this.yuanAmount.toString();
    }

    public String toCustomizedString(boolean isWithCurrencyCode, boolean isWithThousandSplit) {
        String amountStr = null;
        if(isWithThousandSplit) {
            int digits = Currency.getInstance(this.currencyCode).getDefaultFractionDigits();
            NumberFormat nf = DecimalFormat.getInstance();
            nf.setMinimumFractionDigits(digits);
            nf.setMaximumFractionDigits(digits);
            amountStr = nf.format(this.yuanAmount.doubleValue());
        } else {
            amountStr = this.getYuanAmount();
        }

        if(isWithCurrencyCode) {
            amountStr = this.currencyCode + " " + amountStr;
        }

        return amountStr;
    }

    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            Money other = (Money)obj;
            if(this.yuanAmount == null) {
                if(other.yuanAmount != null) {
                    return false;
                }
            } else if(!this.yuanAmount.equals(other.yuanAmount)) {
                return false;
            }

            if(this.currencyCode == null) {
                if(other.currencyCode != null) {
                    return false;
                }
            } else if(!this.currencyCode.equals(other.currencyCode)) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        boolean prime = true;
        byte result = 1;
        int result1 = 31 * result + (this.yuanAmount == null?0:this.yuanAmount.hashCode());
        result1 = 31 * result1 + (this.currencyCode == null?0:this.currencyCode.hashCode());
        return result1;
    }

    public int compareTo(Money other) {
        this.assertSameCurrencyAs(other);
        long thisLongValue = this.getLi();
        long otherLongValue = other.getLi();
        return thisLongValue < otherLongValue?-1:(thisLongValue == otherLongValue?0:1);
    }

    public boolean greaterThan(Money other) {
        return this.compareTo(other) > 0;
    }

    public static Money foreignExchange(Money source, double exchangeRate, String targetCurrencyCode) {
        BigDecimal sourceAmount = new BigDecimal(source.getYuanAmount());
        BigDecimal amountTmp = sourceAmount.multiply(BigDecimal.valueOf(exchangeRate));
        return new Money(targetCurrencyCode, amountTmp);
    }

    public static Money foreignExchangeByDivide(Money source, double exchangeRate, String targetCurrencyCode) {
        boolean targetCurrencyDigits = false;
        int targetCurrencyDigits1;
        if("CNH".equals(targetCurrencyCode)) {
            targetCurrencyDigits1 = 2;
        } else {
            Currency sourceAmount = Currency.getInstance(targetCurrencyCode);
            targetCurrencyDigits1 = sourceAmount.getDefaultFractionDigits();
        }

        if(targetCurrencyDigits1 >= 4) {
            throw new IllegalArgumentException("Currency  is unsupported: " + targetCurrencyCode);
        } else {
            BigDecimal sourceAmount1 = new BigDecimal(source.getYuanAmount());
            BigDecimal amountTmp = sourceAmount1.divide(BigDecimal.valueOf(exchangeRate), targetCurrencyDigits1, 4);
            return new Money(targetCurrencyCode, amountTmp);
        }
    }

    private void assertSameCurrencyAs(Money other) {
        if(!this.currencyCode.equals(other.getCurrencyCode())) {
            throw new IllegalArgumentException("The currency is different.");
        }
    }

    private BigDecimal getLegalAmountFromLi(Currency currency, long li) {
        int digits = currency.getDefaultFractionDigits();
        if(digits >= 4) {
            throw new IllegalArgumentException("Currency  is unsupported: " + currency.getCurrencyCode());
        } else {
            BigDecimal amountTmp = new BigDecimal(li);
            return amountTmp.divide(THOUSAND, currency.getDefaultFractionDigits(), 4);
        }
    }

    private BigDecimal getLegalAmountFromStringAmount(Currency currency, String amount) {
        int digits = currency.getDefaultFractionDigits();
        if(digits >= 4) {
            throw new IllegalArgumentException("Currency  is unsupported: " + currency.getCurrencyCode());
        } else {
            BigDecimal amountTmp = new BigDecimal(amount);
            return amountTmp.divide(BigDecimal.ONE, currency.getDefaultFractionDigits(), 4);
        }
    }

    private Money(String currencyCode, BigDecimal amount) {
        if("CNH".equals(currencyCode)) {
            this.currencyCode = "CNH";
            this.yuanAmount = amount.divide(BigDecimal.ONE, 2, 4);
        } else {
            this.currencyCode = currencyCode;
            Currency currency = Currency.getInstance(currencyCode);
            this.yuanAmount = amount.divide(BigDecimal.ONE, currency.getDefaultFractionDigits(), 4);
        }

    }
}
