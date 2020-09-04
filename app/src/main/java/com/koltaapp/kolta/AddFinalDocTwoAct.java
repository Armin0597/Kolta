package com.koltaapp.kolta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddFinalDocTwoAct extends AppCompatActivity {

    EditText bkt_pendaftaran_pendadaran, toefl, cover_pta, fc_ktp, fc_ijazah, fc_akta;
    ImageView ic_bkt_pendaftaran_pendadaran, ic_toefl, ic_cover_pta, ic_fc_ktp, ic_fc_ijazah, ic_fc_akta;
    Button btn_continue;
    LinearLayout btn_back;

    DatabaseReference reference;
    StorageReference storage;

    Uri doc_location;
    Integer doc_max = 5;

    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    private static final int SELECT_FILE3 = 3;
    private static final int SELECT_FILE4 = 4;
    private static final int SELECT_FILE5 = 5;
    private static final int SELECT_FILE6 = 6;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_final_doc_two);

        getUsernameLocal();

        bkt_pendaftaran_pendadaran = findViewById(R.id.bkt_pendaftaran_pendadaran);
        toefl = findViewById(R.id.toefl);
        cover_pta = findViewById(R.id.cover_pta);
        fc_ktp = findViewById(R.id.fc_ktp);
        fc_akta = findViewById(R.id.fc_akta);
        fc_ijazah = findViewById(R.id.fc_ijazah);
        ic_bkt_pendaftaran_pendadaran = findViewById(R.id.ic_bkt_pendaftaran_pendadaran);
        ic_toefl = findViewById(R.id.ic_toefl);
        ic_cover_pta = findViewById(R.id.ic_cover_pta);
        ic_fc_ktp = findViewById(R.id.ic_fc_ktp);
        ic_fc_akta = findViewById(R.id.ic_fc_akta);
        ic_fc_ijazah = findViewById(R.id.ic_fc_ijazah);
        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);

//        reference = FirebaseDatabase.getInstance().getReference().child("DrafFinal").child(username_key_new).child("berkas");
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                bkt_pendaftaran_pendadaran.setText(dataSnapshot.child("bukti_pendaftaran_pendadaran").getValue().toString());
//                toefl.setText(dataSnapshot.child("toefl").getValue().toString());
//                cover_pta.setText(dataSnapshot.child("cover_pta").getValue().toString());
//                fc_ktp.setText(dataSnapshot.child("scan_ktp").getValue().toString());
//                fc_akta.setText(dataSnapshot.child("scan_akta").getValue().toString());
//                fc_ijazah.setText(dataSnapshot.child("scan_ijazah").getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ic_bkt_pendaftaran_pendadaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE1);
            }
        });
        ic_toefl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE2);
            }
        });
        ic_cover_pta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE3);
            }
        });
        ic_fc_ktp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE4);
            }
        });
        ic_fc_ijazah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE5);
            }
        });
        ic_fc_akta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE6);
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");

                Intent gotofinaldocthree = new Intent(AddFinalDocTwoAct.this, AddFinalDocThreeAct.class);
                startActivity(gotofinaldocthree);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotofinaldocone = new Intent(AddFinalDocTwoAct.this, AddFinalDocAct.class);
                startActivity(gotofinaldocone);
            }
        });

    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //menemukan dokumen
    public void findDoc(int req_code){
        Intent doc = new Intent();
        doc.setType("*/*");
        doc.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(doc, "select file to upload"), req_code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        getUsernameLocal();
        final ProgressDialog pd = new ProgressDialog(this);
        final String nama_tugas_baru = getIntent().getStringExtra("nama_tugas");

        if (resultCode == RESULT_OK) {
            doc_location = data.getData();
            if(requestCode == SELECT_FILE1){
                final String buktiBayar;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                buktiBayar = "brks_"+"bktPendaftaranPendadaran"+"_"+time+"."+getFileExtension(doc_location);
                toefl.setText(buktiBayar);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(buktiBayar);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("bukti_pendaftaran_pendadaran").setValue(buktiBayar);
                                            reference.getRef().child("url_pendaftaran_pendadaran").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE2){
                final String Toefl;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                Toefl = "brks_"+"toefl"+"_"+time+"."+getFileExtension(doc_location);
                toefl.setText(Toefl);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(Toefl);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("toefl").setValue(Toefl);
                                            reference.getRef().child("url_toefl").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE3){
                final String coverPta;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                coverPta = "brks_"+"coverPta"+"_"+time+"."+getFileExtension(doc_location);
                cover_pta.setText(coverPta);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(coverPta);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("cover_pta").setValue(coverPta);
                                            reference.getRef().child("url_cover_pta").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE4){
                final String scanKtp;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                scanKtp = "brks_"+"scanKtp"+"_"+time+"."+getFileExtension(doc_location);
                fc_ktp.setText(scanKtp);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(scanKtp);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("scan_ktp").setValue(scanKtp);
                                            reference.getRef().child("url_scan_ktp").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE5){
                final String scanIjazah;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                scanIjazah = "brks_"+"scanIjazah"+"_"+time+"."+getFileExtension(doc_location);
                fc_ijazah.setText(scanIjazah);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(scanIjazah);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("scan_ijazah").setValue(scanIjazah);
                                            reference.getRef().child("url_scan_ijazah").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }

            }else if(requestCode == SELECT_FILE6){
                final String scanAkta;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                scanAkta = "brks_"+"scanAkta"+"_"+time+"."+getFileExtension(doc_location);
                fc_akta.setText(scanAkta);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(scanAkta);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("scan_akta").setValue(scanAkta);
                                            reference.getRef().child("url_scan_akta").setValue(uri_doc);
                                        }
                                    });

                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pd.dismiss();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage("Percent : " + (int) progressPercent + "%");
                            pd.show();
                        }
                    });
                }
            }

        }
    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY,MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}