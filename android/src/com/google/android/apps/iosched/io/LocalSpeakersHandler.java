package com.google.android.apps.iosched.io;

import static com.google.android.apps.iosched.util.ParserUtils.sanitizeId;
import static com.google.android.apps.iosched.util.ParserUtils.AtomTags.ENTRY;
import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.util.Log;
import android.widget.Toast;

import com.google.android.apps.iosched.provider.ScheduleContract;
import com.google.android.apps.iosched.provider.ScheduleContract.Speakers;
import com.google.android.apps.iosched.provider.ScheduleContract.SyncColumns;
import com.google.android.apps.iosched.util.Lists;

public class LocalSpeakersHandler extends XmlHandler {

	 private static final String TAG = "SpeakersHandler";

	    public LocalSpeakersHandler() {
	        super(ScheduleContract.CONTENT_AUTHORITY);
	    }

	    /** {@inheritDoc} */
	    @Override
	    public ArrayList<ContentProviderOperation> parse(XmlPullParser parser, ContentResolver resolver)
	            throws XmlPullParserException, IOException {
	        final ArrayList<ContentProviderOperation> batch = Lists.newArrayList();
	        // Walk document, parsing any incoming entries
	        int type;
	        while ((type = parser.next()) != END_DOCUMENT) {
	            if (type == START_TAG && Tags.SPEAKER.equals(parser.getName())) {
	                // Process single spreadsheet row at a time
	                final int depth = parser.getDepth();
	                final ContentProviderOperation.Builder builder = ContentProviderOperation
	                        .newInsert(Speakers.CONTENT_URI);
	                
	                String tag = null;
	                String speakerId=null;
	                String imagePath="";
	                String company="";
	                String bio="";
	                String URL="";
	                while (((type = parser.next()) != END_TAG ||
	                        parser.getDepth() > depth) && type != END_DOCUMENT) {
	                    if (type == START_TAG) {
	                        tag = parser.getName();
	                    } else if (type == END_TAG) {
	                        tag = null;
	                    } else if (type == TEXT) {
	                        final String text = parser.getText();
	                        if (Tags.NAME.equals(tag)) {
	                            speakerId = text;
	                        } else if (Tags.IMAGE.equals(tag)) {
	                            imagePath = text;
	                        } else if (Tags.COMPANY.equals(tag)) {
	                            company = text;
	                        } else if (Tags.ABSTRACT.equals(tag)) {
	                        	bio = text;
	                        } else if(Tags.URL.equals(tag)){
	                        	URL = text;
	                        }
	                    }
	                }
	                
	                builder.withValue(SyncColumns.UPDATED, 0);
	                builder.withValue(Speakers.SPEAKER_ID, sanitizeId(speakerId, true));
	                builder.withValue(Speakers.SPEAKER_NAME, speakerId);
	                builder.withValue(Speakers.SPEAKER_IMAGE_URL, imagePath);
	                builder.withValue(Speakers.SPEAKER_COMPANY, company);
	                builder.withValue(Speakers.SPEAKER_ABSTRACT, bio);
	                builder.withValue(Speakers.SPEAKER_URL, URL);

	                // Normal speaker details ready, write to provider
	                batch.add(builder.build());
	            }
	        }
	        return batch;
	    }
	    
	    interface Tags {
	        String SPEAKER = "speaker";
	        String NAME = "name";
	        String IMAGE = "image";
	        String COMPANY = "company";
	        String ABSTRACT = "abstract";
	        String URL = "url";
	    }

	    /** Columns coming from remote spreadsheet. 
	    private interface Columns {
	        String SPEAKER_TITLE = "speakertitle";
	        String SPEAKER_IMAGE_URL = "speakerimageurl";
	        String SPEAKER_COMPANY = "speakercompany";
	        String SPEAKER_ABSTRACT = "speakerabstract";
	        String SPEAKER_URL = "speakerurl";

	        // speaker_title: Aaron Koblin
	        // speaker_image_url: http://path/to/image.png
	        // speaker_company: Google
	        // speaker_abstract: Aaron takes social and infrastructural data and uses...
	        // speaker_url: http://profiles.google.com/...

	    }*/
}
