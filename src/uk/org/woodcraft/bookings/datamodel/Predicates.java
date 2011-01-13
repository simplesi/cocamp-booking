package uk.org.woodcraft.bookings.datamodel;

import org.apache.commons.collections.Predicate;

public class Predicates {

	public static class TransactionTypePredicate implements Predicate
	{
		private TransactionType transactionType;

		public TransactionTypePredicate(TransactionType type) {
			transactionType = type;
		}
		
		@Override
		public boolean evaluate(Object object) {
			if (! (object instanceof Transaction)) return false;
			
			return ((Transaction)object).getType().equals(transactionType);
		}
		
	}
}
