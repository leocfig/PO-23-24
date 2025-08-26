all:
	(cd xxl-core; make $(MFLAGS) all)
	(cd xxl-app; make $(MFLAGS) all)

clean:
	(cd xxl-core; make $(MFLAGS) clean)
	(cd xxl-app; make $(MFLAGS) clean)

install:
	(cd xxl-core; make $(MFLAGS) install)
	(cd xxl-app; make $(MFLAGS) install)
run:
	(java -cp "xxl-app/xxl-app.jar:xxl-core/xxl-core.jar:../po-uilib/po-uilib.jar" xxl.app.App)
