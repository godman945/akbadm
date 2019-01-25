<<<<<<< HEAD
server=lsbidx.mypchome.com.tw
xml_dir="/home/webuser/multicore/akb_keyword/data/xml/"
xml_path="$xml_dir*.xml"

ssh webuser@$server mkdir -p $xml_dir
rsync -avP -e ssh $xml_path webuser@$server:$xml_dir
rm -rf $xml_path
=======
rsync -avP -e ssh /home/webuser/multicore/akb_keyword/data/xml webuser@lsbidxstg.mypchome.com.tw:/home/webuser/multicore/akb_keyword/data/
rm -rf /home/webuser/multicore/akb_keyword/data/xml
>>>>>>> eeecbb69fd89b489a1e6dc5c5b9488201cdbfcef
