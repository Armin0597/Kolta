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

public class AddFinalDocThreeAct extends AppCompatActivity {

    EditText data_pribadi, penulisan_nama, srt_ket_10, srt_rekomend_kaprodi, scan_logbook, scan_berita_acara, laporan_final, ukuran_toga;
    ImageView ic_data_pribadi, ic_penulisan_nama, ic_srt_ket_10, ic_srt_rekomend_kaprodi, ic_scan_logbook, ic_scan_berita_acara, ic_laporan_final;
    Button btn_save;
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
    private static final int SELECT_FILE7 = 7;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_final_doc_three);

        getUsernameLocal();

        data_pribadi = findViewById(R.id.data_pribadi);
        penulisan_nama = findViewById(R.id.penulisan_nama);
        srt_ket_10 = findViewById(R.id.srt_ket_10);
        srt_rekomend_kaprodi = findViewById(R.id.srt_rekomend_kaprodi);
        scan_logbook = findViewById(R.id.scan_logbook);
        scan_berita_acara = findViewById(R.id.scan_berita_acara);
        laporan_final = findViewById(R.id.laporan_final);
        ukuran_toga = findViewById(R.id.ukuran_toga);
        ic_data_pribadi = findViewById(R.id.ic_data_pribadi);
        ic_penulisan_nama = findViewById(R.id.ic_penulisan_nama);
        ic_srt_ket_10 = findViewById(R.id.ic_srt_ket_10);
        ic_srt_rekomend_kaprodi = findViewById(R.id.ic_srt_rekomend_kaprodi);
        ic_scan_logbook = findViewById(R.id.ic_scan_logbook);
        ic_scan_berita_acara = findViewById(R.id.ic_scan_berita_acara);
        ic_laporan_final = findViewById(R.id.ic_laporan_final);
        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);

        ic_data_pribadi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE1);
            }
        });
        ic_penulisan_nama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE2);
            }
        });
        ic_srt_ket_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE3);
            }
        });
        ic_srt_rekomend_kaprodi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE4);
            }
        });
        ic_scan_logbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE5);
            }
        });
        ic_scan_berita_acara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE6);
            }
        });
        ic_laporan_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDoc(SELECT_FILE7);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_save.setEnabled(false);
                btn_save.setText("Loading ...");

                reference = FirebaseDatabase.getInstance().getReference().child("DrafFinal").child(username_key_new).child("berkas");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference.getRef().child("ukuran_toga").setValue(ukuran_toga.getText().toString());

                        Intent gotodraffinal = new Intent(AddFinalDocThreeAct.this, FinalDocAct.class);
                        startActivity(gotodraffinal);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotofinaldoctwo = new Intent(AddFinalDocThreeAct.this, AddFinalDocTwoAct.class);
                startActivity(gotofinaldoctwo);
            }
        });

    }


    //mendapatkan ekstensi file
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

        if (resultCode == RESULT_OK) {
            doc_location = data.getData();

            if(requestCode == SELECT_FILE1){
                final String dataPribadi;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                dataPribadi = "brks_"+"dataPribadi"+"_"+time+"."+getFileExtension(doc_location);
                srt_ket_10.setText(dataPribadi);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(dataPribadi);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("data_pribadi").setValue(dataPribadi);
                                            reference.getRef().child("url_data_pribadi").setValue(uri_doc);
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
                    final String penulisanNama;
                    long yourmilliseconds = System.currentTimeMillis();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                    Date resultdate = new Date(yourmilliseconds);
                    String time = simpleDateFormat.format(resultdate);
                    penulisanNama = "brks_"+"penulisanNama"+"_"+time+"."+getFileExtension(doc_location);
                    srt_ket_10.setText(penulisanNama);

                    reference = FirebaseDatabase.getInstance().getReference()
                            .child("DrafFinal").child(username_key_new).child("berkas");
                    storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                    //validasi file (apakah ada?)
                    if(doc_location != null) {
                        final StorageReference storageReference =
                                storage.child(penulisanNama);

                        storageReference.putFile(doc_location)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                pd.dismiss();
                                                String uri_doc = uri.toString();
                                                reference.getRef().child("penulisan_nama").setValue(penulisanNama);
                                                reference.getRef().child("url_penulisan_nama").setValue(uri_doc);
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
                final String srtKet10;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                srtKet10 = "brks_"+"srtKet10"+"_"+time+"."+getFileExtension(doc_location);
                srt_ket_10.setText(srtKet10);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(srtKet10);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("surat_keterangan_10").setValue(srtKet10);
                                            reference.getRef().child("url_srt_keterangan_10").setValue(uri_doc);
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
                final String suratRekomenProdi;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                suratRekomenProdi = "brks_"+"suratRekomenProdi"+"_"+time+"."+getFileExtension(doc_location);
                srt_rekomend_kaprodi.setText(suratRekomenProdi);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(suratRekomenProdi);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("surat_rekomen_prodi").setValue(suratRekomenProdi);
                                            reference.getRef().child("url_srt_rekomen_prodi").setValue(uri_doc);
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
                final String scanLogbook;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                scanLogbook = "brks_"+"scanlogbook"+"_"+time+"."+getFileExtension(doc_location);
                scan_logbook.setText(scanLogbook);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(scanLogbook);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("scan_logbook").setValue(scanLogbook);
                                            reference.getRef().child("url_scan_logbook").setValue(uri_doc);
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
                final String scanBeritaAcara;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                scanBeritaAcara = "brks_"+"scanBeritaAcara"+"_"+time+"."+getFileExtension(doc_location);
                scan_berita_acara.setText(scanBeritaAcara);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(scanBeritaAcara);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("scan_berita_acara").setValue(scanBeritaAcara);
                                            reference.getRef().child("url_scan_berita_acara").setValue(uri_doc);
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

            }else if(requestCode == SELECT_FILE7){
                final String laporanFinal;
                long yourmilliseconds = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyykkmm");
                Date resultdate = new Date(yourmilliseconds);
                String time = simpleDateFormat.format(resultdate);
                laporanFinal = "brks_"+"laporanFinal"+"_"+time+"."+getFileExtension(doc_location);
                laporan_final.setText(laporanFinal);

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("DrafFinal").child(username_key_new).child("berkas");
                storage = FirebaseStorage.getInstance().getReference().child("Drafusers").child(username_key_new).child("BerkasFinal");

                //validasi file (apakah ada?)
                if(doc_location != null) {
                    final StorageReference storageReference =
                            storage.child(laporanFinal);

                    storageReference.putFile(doc_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            pd.dismiss();
                                            String uri_doc = uri.toString();
                                            reference.getRef().child("laporan_final").setValue(laporanFinal);
                                            reference.getRef().child("url_laporan_final").setValue(uri_doc);
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