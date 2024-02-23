public class Palindrome {
    public Deque<Character> wordToDeque(String word){
        Deque<Character> deque = new LinkedListDeque<>();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    /**
     * The isPalindrome method should return true if the given word is a palindrome, and false otherwise. 
     * A palindrome is defined as a word that is the same whether it is read forwards or backwards. 
     */
    public boolean isPalindrome(String word){
        if(word == null || word.length() <= 1){
            return true;
        }
        Deque<Character> deque = wordToDeque(word);
        return isPalindrome(deque);
    }
    private boolean isPalindrome(Deque<Character> deque){
        if ( (deque.size() == 1) || (deque.size() == 0) ){
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if(first != last){
            return false;
        }else{
            return isPalindrome(deque);
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc){
        if(word == null || word.length() <= 1){
            return true;
        }
        Deque<Character> deque = wordToDeque(word);
        return isPalindrome(deque, cc);
    }

    private boolean isPalindrome(Deque<Character> deque, CharacterComparator cc){
        if ( (deque.size() == 1) || (deque.size() == 0) ){
            return true;
        }
        char first = deque.removeFirst();
        char last = deque.removeLast();
        if (! cc.equalChars(first, last)){
            return false;
        }else{
            return isPalindrome(deque, cc);
        }
    }

}
