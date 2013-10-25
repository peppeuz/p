package com.peppeuz.faceapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class MainActivity extends Activity implements OnClickListener{
	//
	Button post;
	Context ctx;
	TextView nome;
	Boolean logged;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nome = (TextView) findViewById(R.id.nome);
		post = (Button) findViewById(R.id.post);
		post.setOnClickListener(this);
		ctx =this;
		logged = false;
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v==post){
			condividi();
		}
		// TODO Auto-generated method stub
		
	}
	
	public void facebookLogin(){
	
	    Session.openActiveSession(this, true, new Session.StatusCallback() {

	
	      @Override
	      public void call(Session session, SessionState state, Exception exception) {
	        if (session.isOpened()) {
	        	Request.newMeRequest(session, new Request.GraphUserCallback() {
		            @Override
		            public void onCompleted(final GraphUser user, final Response response) {
		              if (user != null) {		                
		                nome.setText("Ciao " + user.getName() + "!");
		                logged = true;
		              }
		            }
		          }).executeAsync();
	        			
	        	
	        	
	}
	        else
	        {
	        	nome.setText("Ciao anonimo!");
	        	logged = false;
	        }
	      }});
	}

	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	      facebookLogin();
	  }
	 
	 
	 public void condividi(){
		 Bundle params = new Bundle();
			params.putString("name", "AndroidWorld - DevCorner: effettuiamo il login con Facebook e postiamo sulla bacheca");
			params.putString("caption", "AndroidWorld: il primo sito italiano dedicato ad Android!");
			params.putString("description",
					"DevCorner: l'appuntamento settimanale per imparare a programmare su Android");
			params.putString("link", "http://www.androidworld.it/2013/09/03/devcorner-effettuiamo-il-login-con-facebook-e-postiamo-sulla-bacheca");
			params.putString("picture", "http://www.androidworld.it/wp-content/themes/android2013/images/logo_def.png");
			if ((Session.getActiveSession() == null)|| (logged==false)) {
				Toast.makeText(ctx,
						"Non sei ancora collegato su Facebbok: esegui il login per condividere la nota!",
						Toast.LENGTH_LONG).show();
			} 
			
			else {
				try{
				WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(ctx,
						Session.getActiveSession(), params)).setOnCompleteListener(
						new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error == null) {
									
									final String postId = values
											.getString("post_id");
									if (postId != null) {
										Toast.makeText(ctx,
												"Post effettuato correttamente!" 
										//+ postId
												
												,Toast.LENGTH_SHORT).show();
									} else {

										Toast.makeText(
												ctx.getApplicationContext(),
												"Post annullato",
												Toast.LENGTH_SHORT).show();
									}
								} else if (error instanceof FacebookOperationCanceledException) {
									// 
									Toast.makeText(
											ctx.getApplicationContext(),
											"Post annullato", Toast.LENGTH_SHORT)
											.show();
								} else {

									Toast.makeText(
											ctx.getApplicationContext(),
											"Si è presentato un errore durante la pubblicazione",
											Toast.LENGTH_SHORT).show();
								}
							}

						}).build();
				feedDialog.show();
			}
			
			catch (FacebookException e) {
				// TODO: handle exception
				Toast.makeText(
						ctx.getApplicationContext(),
						"Impossibile pubblicare",
						Toast.LENGTH_SHORT).show();
				
			}
			}
		 
	 }

}
