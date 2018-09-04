import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.data.Word;
import net.sf.extjwnl.dictionary.Dictionary;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.NPPhraseSpec;
import simplenlg.phrasespec.PPPhraseSpec;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestMain {

    public static void main(String[] args) {

        String subjectTerrain = "mountain";

        String verbTerrain = "tower";

        String objectRange = "distance";

        try {
            Dictionary dictionary = Dictionary.getDefaultResourceInstance();

            IndexWord indexedVerbTerrain = dictionary.getIndexWord(POS.VERB, verbTerrain);

            final List verbOptions = new ArrayList<String>();

            for (long synsetOffset : indexedVerbTerrain.getSynsetOffsets()) {
                final Synset synset = dictionary.getSynsetAt(POS.VERB, synsetOffset);
                for (Word synWord : synset.getWords()) {
                    verbOptions.add(synWord.getLemma());
                }
            }

            Random random = new Random();
            verbTerrain = (String) verbOptions.get(random.nextInt(verbOptions.size()));

            Lexicon lexicon = Lexicon.getDefaultLexicon();
            NLGFactory nlgFactory = new NLGFactory(lexicon);
            Realiser realiser = new Realiser(lexicon);


            NPPhraseSpec terrain = nlgFactory.createNounPhrase(subjectTerrain);
            terrain.setDeterminer("the");

            NPPhraseSpec place = nlgFactory.createNounPhrase(objectRange);
            place.setDeterminer("the");
            PPPhraseSpec pp = nlgFactory.createPrepositionPhrase();
            pp.addComplement(place);
            pp.setPreposition("in");

            SPhraseSpec p = nlgFactory.createClause(terrain, verbTerrain, pp);

            String output = realiser.realiseSentence(p);
            System.out.println(output);

        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }
}
