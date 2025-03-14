import React from 'react';
import Image from "next/image";

const Loading: React.FC = () => (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
        <Image
            src="/load-block.gif"
            alt="Loading..."
            width={70}
            height={70}
        />
    </div>
);

export default Loading;
